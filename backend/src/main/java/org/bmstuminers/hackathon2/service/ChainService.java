package org.bmstuminers.hackathon2.service;

import org.bmstuminers.hackathon2.exception.ProcessingException;
import org.bmstuminers.hackathon2.exception.WrongParameterException;
import org.bmstuminers.hackathon2.model.*;
import org.bmstuminers.hackathon2.repo.CachedDatasetLineRepository;
import org.bmstuminers.hackathon2.repo.CachedDatasetRepository;
import org.bmstuminers.hackathon2.repo.ChainRepository;
import org.bmstuminers.hackathon2.service.block.Block;
import org.bmstuminers.hackathon2.service.block.DatasetLoader;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final CachedDatasetLineRepository cachedRepository;
    private final CachedDatasetRepository cachedDatasetRepository;

    /**
     * Constructs new instance.
     * All available blocks should be registered in availableBlocks list in constructor
     * @param datasetService dataset service
     * @param chainRepository chain repos
     * @param cachedRepository
     * @param cachedDatasetRepository
     */
    @Autowired
    public ChainService(DatasetService datasetService, ChainRepository chainRepository,
                        CachedDatasetLineRepository cachedRepository, CachedDatasetRepository cachedDatasetRepository) {
        this.chainRepository = chainRepository;
        this.datasetService = datasetService;
        this.cachedRepository = cachedRepository;
        this.cachedDatasetRepository = cachedDatasetRepository;

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
        checkBlock(chain);
        return chainRepository.save(chain);
    }

    private void checkBlock(Chain chain) throws WrongParameterException {
        for (BlockInfo info: chain.getBlocks()) {
            Block block = findByName(info.getName());
            block.checkParams(info.getParams());
        }
    }

    /**
     * Gets all chains
     * @return all chains
     */
    public List<Chain> findAll() {
        return chainRepository.findAll();
    }

    /**
     * Deletes chain
     * @param id chain id
     */
    public void delete(String id) {
        chainRepository.delete(id);
        cachedRepository.removeByChainId(id);
        cachedDatasetRepository.removeByChainId(id);
    }

    /**
     * Executed the given chain
     * @param id chain id
     * @param page page number to be shown
     * @param pageSize size of the page
     * @return processed dataset
     * @throws WrongParameterException
     * @throws ProcessingException
     */
    public Dataset execute(String id, int page, int pageSize) throws WrongParameterException, ProcessingException {
        Chain chain = chainRepository.findOne(id);
        if (chain == null) {
            throw new IllegalArgumentException("Wrong chain id: " + chain);
        }
        Page<CachedDatasetLine> linePage = cachedRepository.findByChainId(id, new PageRequest(page, pageSize));
        CachedDatasetInfo cachedInfo = cachedDatasetRepository.findOneByChainId(id);
        if (linePage == null || linePage.getContent().size() == 0 || cachedInfo == null) {
            cachedRepository.removeByChainId(id);
            cachedDatasetRepository.removeByChainId(id);
            Dataset dataset = null;
            for (BlockInfo info : chain.getBlocks()) {
                Block block = findByName(info.getName());
                dataset = block.process(dataset, info.getParams());
            }
            for (List<String> row: dataset.getPage()) {
                CachedDatasetLine line = new CachedDatasetLine(id, row);
                cachedRepository.save(line);
            }
            CachedDatasetInfo info = new CachedDatasetInfo();
            info.setChainId(id);
            info.setFields(dataset.getFields());
            cachedDatasetRepository.save(info);

            linePage = cachedRepository.findByChainId(id, new PageRequest(page, pageSize));
            cachedInfo = cachedDatasetRepository.findOneByChainId(id);
        }
        Dataset dataset = new Dataset();
        dataset.setFields(cachedInfo.getFields());
        List<List<String>> data = new ArrayList<>();
        for (CachedDatasetLine line: linePage.getContent()) {
            data.add(line.getData());
        }
        dataset.setPage(data);
        return dataset;
    }

    public Chain update(String id, List<BlockInfo> blocks) throws WrongParameterException {
        Chain chain = chainRepository.findOne(id);
        if (chain == null) {
            throw new IllegalArgumentException("Wrong chain id: " + chain);
        }
        chain.setBlocks(blocks);
        checkBlock(chain);
        cachedRepository.removeByChainId(id);
        cachedDatasetRepository.removeByChainId(id);
        return chainRepository.save(chain);
    }
}
