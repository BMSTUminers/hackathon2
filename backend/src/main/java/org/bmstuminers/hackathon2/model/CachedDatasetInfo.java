package org.bmstuminers.hackathon2.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Konstantin Grechishchev
 */
@Document
public class CachedDatasetInfo extends DatasetInfo {

    @Indexed
    private String chainId;

    public CachedDatasetInfo() {
        setId(new ObjectId().toString());
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }
}

