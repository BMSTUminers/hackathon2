package org.bmstuminers.hackathon2.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Line of the dataset
 * @author Konstantin Grechishchev
 */
@Document
public class DatasetLine {

    @Id
    private String id;
    @Indexed
    private String datasetId;
    private List<String> data;

    public DatasetLine() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }
}
