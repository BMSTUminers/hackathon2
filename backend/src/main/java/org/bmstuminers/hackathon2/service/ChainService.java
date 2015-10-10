package org.bmstuminers.hackathon2.service;

import org.bmstuminers.hackathon2.exception.WrongParameterException;
import org.bmstuminers.hackathon2.model.BlockInfo;
import org.bmstuminers.hackathon2.model.Chain;
import org.bmstuminers.hackathon2.model.Dataset;
import org.bmstuminers.hackathon2.model.DatasetInfo;
import org.bmstuminers.hackathon2.repo.ChainRepository;
import org.bmstuminers.hackathon2.service.block.Block;
import org.bmstuminers.hackathon2.service.block.DatasetLoader;
import org.bson.types.ObjectId;
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

    private final DatasetService datasetService;
    private final ChainRepository chainRepository;

    /**
     * Constructs new instance.
     * All available blocks should be registered in availableBlocks list in constructor
     * @param datasetService dataset service
     * @param chainRepository chain repos
     */
    @Autowired
    public ChainService(DatasetService datasetService, ChainRepository chainRepository) {
        this.chainRepository = chainRepository;
        this.datasetService = datasetService;

        availableBlocks.add(new DatasetLoader(this.datasetService));
    }

    public List<Block> getAvailableBlocks() {
        return availableBlocks;
    }

    /**
     * Gets and available block by name
     * @param name block name
     * @return block
     */
    public Block findByName(String name) {
        for (Block block: availableBlocks) {
            if (block.getName().equals(name)) {
                return block;
            }
        }
        throw new IllegalArgumentException("Unknown block name: " + name);
    }

    /**
     * Creates a new chain
     * @param blocks blocks info
     * @return saved chain
     */
    public Chain create(List<BlockInfo> blocks) throws WrongParameterException {
        Chain chain = new Chain();
        chain.setId(new ObjectId().toString());
        chain.setBlocks(blocks);
        for (BlockInfo info: blocks) {
            Block block = findByName(info.getName());
            block.checkParams(info.getParams());
        }
        return chainRepository.save(chain);
    }
}
