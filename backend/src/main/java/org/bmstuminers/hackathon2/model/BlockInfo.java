package org.bmstuminers.hackathon2.model;

import java.util.Map;

/**
 * Information about the block of the chain
 * @author Konstantin Grechishchev
 */
public class BlockInfo {

    /**
     * Name (id) of the block. Should match the name in Block class
     */
    private String name;
    /**
     * Params map
     */
    private Map<String, String> params;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
