package org.bmstuminers.hackathon2.service;

import org.bmstuminers.hackathon2.model.Dataset;
import org.bmstuminers.hackathon2.service.block.Block;
import org.bmstuminers.hackathon2.service.block.DatasetLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Konstantin Grechishchev
 */
@Service
public class ChainService {

    private final List<Block> availableBlocks = new ArrayList<>();

    /**
     * Constructs new instance.
     * All available blocks should be registered in availableBlocks list in constructor
     * @param datasetService dataset service
     */
    @Autowired
    public ChainService(DatasetService datasetService) {
        availableBlocks.add(new DatasetLoader(datasetService));
    }

    public List<Block> getAvailableBlocks() {
        return availableBlocks;
    }
}
