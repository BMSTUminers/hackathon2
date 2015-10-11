package org.bmstuminers.hackathon2.service.block;

import org.bmstuminers.hackathon2.exception.ProcessingException;
import org.bmstuminers.hackathon2.model.Dataset;
import org.bmstuminers.hackathon2.model.ParameterDescription;
import org.bmstuminers.hackathon2.service.DatasetService;

import java.util.*;

/**
 * Created by generall on 10.10.15.
 */
public class AverageLoader extends AbstractBlock {
    private final static String NAME = "averager";
    private final static String DESCRIPTION = "Calc average values";


    private final String COLUMNS = "columns";
    private final String GROUP_BY = "groupBy";

    private final List<ParameterDescription> parameterDescriptions = new ArrayList<>();
    private final DatasetService datasetService;

    public AverageLoader(DatasetService datasetService) {
        super(NAME, DESCRIPTION);
        this.datasetService = datasetService;
        ParameterDescription pd = new ParameterDescription(
                COLUMNS,
                "comma separated list of column names to be calculated"
        );
        parameterDescriptions.add(pd);
        pd = new ParameterDescription(
                GROUP_BY,
                "comma separated list of column names grouped by. Optional. Default - none",
                true,
                ""
        );
        parameterDescriptions.add(pd);
    }

    @Override
    protected Dataset doProcess(Dataset input, Map<String, String> params) throws ProcessingException {

        String[] columns_ave;
        int[] columnIds_ave;
        try {

            String columns_ave_str = params.get(COLUMNS);

            columns_ave = columns_ave_str.split(",");

            columnIds_ave = new int[columns_ave.length];
            for (int i = 0; i<columns_ave.length; i++) {
                columnIds_ave[i] = findByName(input.getFields(), columns_ave[i]);
            }
        } catch (IllegalArgumentException e) {
            throw new ProcessingException("Error during dataset loading", e);
        }
        String groupFieldsData = params.get(GROUP_BY);

        String[] columns_group = {};
        int[] columnIds_group = {};

        if (!groupFieldsData.isEmpty()) {
            columns_group = groupFieldsData.split(",");
            columnIds_group = new int[columns_group.length];

            for (int i = 0; i < columns_group.length; i++) {
                columnIds_group[i] = findByName(input.getFields(), columns_group[i]);
            }
        }

        String key_separator = "___";

        Map<String, Map<String, Double> > aves_map = new HashMap<>();
        Map<String, Map<String, Integer> > counts_map = new HashMap<>();

        /*
        for(String ave_col: columns_ave){
            aves_map.put(ave_col, new HashMap<>());
            counts_map.put(ave_col, new HashMap<>());
        }*/

        /*
        val1___val2 --> sum1 - > \Sigma
                        sum2 - > \Sigma
         */

        for (List<String> row: input.getPage()) {
            // 1 row of data, obtain by ID
            String key = "";
            for (int index: columnIds_group) {
                key += key_separator + row.get(index);
            }
            if (!aves_map.containsKey(key)) {
                aves_map.put(key, new HashMap<>());
                counts_map.put(key, new HashMap<>());
            }
            for(int i = 0; i < columns_ave.length; i++) {
                // columns need to be calc:
                int col_id = columnIds_ave[i];
                String col_name = columns_ave[i];


                Map<String, Double> aves = aves_map.get(key);
                Map<String, Integer> counts = counts_map.get(key);

                Double val = Double.parseDouble(row.get(col_id));
                if (aves.containsKey(col_name)) {
                    aves.put(col_name, aves.get(col_name) + val);
                    counts.put(col_name, counts.get(col_name) + 1);
                } else {
                    aves.put(col_name, val);
                    counts.put(col_name, 1);
                }
            }
        }

        Dataset counted = new Dataset();

        ArrayList<String> res_fields = new ArrayList<>(Arrays.asList(columns_group));
        res_fields.addAll(Arrays.asList(columns_ave));
        counted.setFields(res_fields);

        List<List<String>> countedPage = new ArrayList<>();
        for(String key: aves_map.keySet()){
            List<String> countedRow = new ArrayList<>();
            String[] values = key.split(key_separator);
            for(int idx = 1; idx < values.length; idx++){
                // values[0] = always empty string
                countedRow.add(values[idx]);
            }
            Map<String, Double> aves = aves_map.get(key);
            Map<String, Integer> counts = counts_map.get(key);
            for(String count_col: aves.keySet()){
                countedRow.add( Double.toString( aves.get(count_col) / counts.get(count_col)) );
            }
            countedPage.add(countedRow);
        }
        counted.setPage(countedPage);

        return counted;
    }

    @Override
    public List<ParameterDescription> getParamsDescription() {
        return parameterDescriptions;
    }

    private int findByName(List<String> list, String name) throws ProcessingException {
        int i = 0;
        for (String el: list) {
            if (el.equals(name)) {
                return i;
            }
            i++;
        }
        throw new ProcessingException("Field " + name + " is missing!");
    }
}
