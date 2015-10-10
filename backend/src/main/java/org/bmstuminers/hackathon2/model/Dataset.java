package org.bmstuminers.hackathon2.model;

import java.util.List;

/**
 * @author Konstantin Grechishchev
 */
public class Dataset {

    private List<String> fields;
    private Iterable<List<String>> page;

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public Iterable<List<String>> getPage() {
        return page;
    }

    public void setPage(Iterable<List<String>> page) {
        this.page = page;
    }
}
