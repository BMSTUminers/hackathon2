package org.bmstuminers.hackathon2.model;

import java.util.List;

/**
 * @author Konstantin Grechishchev
 */
public class Dataset {

    private List<String> fields;
    private List<List<String>> page;

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<List<String>> getPage() {
        return page;
    }

    public void setPage(List<List<String>> page) {
        this.page = page;
    }
}
