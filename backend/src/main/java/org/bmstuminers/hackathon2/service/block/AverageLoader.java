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

        int ave_column;
        try {
            ave_column = findByName(input.getFields(), params.get(COLUMNS));
        } catch (IllegalArgumentException e) {
            throw new ProcessingException("Error during dataset loading", e);
        }
        String groupFieldsData = params.get(GROUP_BY);

        String[] columns = groupFieldsData.split(",");
        int[] columnIds = new int[columns.length];
        for (int i = 0; i<columns.length; i++) {
            columnIds[i] = findByName(input.getFields(), columns[i]);
        }

        String key_separator = "___";

        Map<String, Double> aves = new HashMap<>();
        Map<String, Integer> counts = new HashMap<>();


        for (List<String> row: input.getPage()) {
            String key = "";
            for (int index: columnIds) {
                key += key_separator + row.get(index);
            }
            Double val = Double.parseDouble(row.get(ave_column));
            if(aves.containsKey(key))
            {
                aves.put( key, aves.get(key) + val );
                counts.put(key, counts.get(key) + 1);
            }else{
                aves.put(key, val);
                counts.put(key, 1);
            }
        }


        Dataset filtered = new Dataset();
        filtered.setFields(Arrays.asList(columns));

        return filtered;
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
