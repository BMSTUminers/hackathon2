package org.bmstuminers.hackathon2.service.block;

import org.bmstuminers.hackathon2.exception.ProcessingException;
import org.bmstuminers.hackathon2.model.Dataset;
import org.bmstuminers.hackathon2.model.ParameterDescription;
import org.bmstuminers.hackathon2.service.DatasetService;
import org.springframework.data.domain.Page;

import java.util.*;

/**
 * Base block to load required fields from data source
 * @author Konstantin Grechishchev
 */
public class DatasetLoader extends AbstractBlock {

    private final static String NAME = "loader";
    private final static String DESCRIPTION = "Loads provided dataset";

    private final String DATA_SOURCE_ID = "datasourceId";
    private final String COLUMNS = "columns";

    private final List<ParameterDescription> parameterDescriptions = new ArrayList<>();
    private final DatasetService datasetService;

    public DatasetLoader(DatasetService datasetService) {
        super(NAME, DESCRIPTION);
        this.datasetService = datasetService;
        ParameterDescription pd = new ParameterDescription(
                DATA_SOURCE_ID,
                "The id of the datasource to load the data from"
        );
        parameterDescriptions.add(pd);
        pd = new ParameterDescription(
                COLUMNS,
                "comma separated list of column names to be loaded. Optional. Default - all dataset",
                true,
                ""
        );
        parameterDescriptions.add(pd);
    }

    @Override
    protected Dataset doProcess(Dataset input, Map<String, String> params) throws ProcessingException {
        Dataset dataset;
        try {
            dataset = datasetService.getData(params.get(DATA_SOURCE_ID));
        } catch (IllegalArgumentException e) {
            throw new ProcessingException("Error during dataset loading", e);
        }
        String fieldsData = params.get(COLUMNS);
        if (fieldsData.isEmpty()) {
            return dataset;
        }
        String[] columns = fieldsData.split(",");
        int[] columnIds = new int[columns.length];
        for (int i = 0; i<columns.length; i++) {
            columnIds[i] = findByName(dataset.getFields(), columns[i]);
        }
        List<List<String>> filteredPage = new ArrayList<>();
        for (List<String> row: dataset.getPage()) {
            List<String> filteredRow = new ArrayList<>();
            for (int index: columnIds) {
                filteredRow.add(row.get(index));
            }
            filteredPage.add(filteredRow);
        }

        Dataset filtered = new Dataset();
        filtered.setFields(Arrays.asList(columns));
        filtered.setPage(filteredPage);

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
