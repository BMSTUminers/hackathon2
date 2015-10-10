package org.bmstuminers.hackathon2.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Chain class
 * @author Konstantin Grechishchev
 */
@Document
public class Chain {

    /**
     * Chain id
     */
    @Id
    private String id;
    /**
     * Chain blocks
     */
    private List<BlockInfo> blocks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<BlockInfo> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<BlockInfo> blocks) {
        this.blocks = blocks;
    }
}
