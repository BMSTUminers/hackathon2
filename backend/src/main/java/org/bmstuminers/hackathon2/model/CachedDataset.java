package org.bmstuminers.hackathon2.model;

import org.springframework.data.annotation.Id;

/**
 * Cached dataset
 * @author Konstantin Grechishchev
 */
public class CachedDataset extends Dataset {

    /**
     * Collection id
     */
    @Id
    public String id;

    public CachedDataset() {  }

    public CachedDataset(String id, Dataset dataset) {
        this.id = id;
        this.setPage(dataset.getPage());
        this.setFields(dataset.getFields());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
