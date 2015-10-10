package org.bmstuminers.hackathon2.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Dataset line of the output (cached) dataset
 * @author Konstantin Grechishchev
 */
@Document
public class CachedDatasetLine extends DatasetLine {

    @Indexed
    private String chainId;

    public CachedDatasetLine() { }

    public CachedDatasetLine(String chainId, CachedDatasetLine line) {
        this.setId(new ObjectId().toString());
        this.chainId = chainId;
        this.setData(line.getData());
    }

    public CachedDatasetLine(String chainId, List<String> data) {
        this.setId(new ObjectId().toString());
        this.chainId = chainId;
        this.setData(data);
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }
}
