package org.bmstuminers.hackathon2.service;

import org.bmstuminers.hackathon2.model.Dataset;
import org.bmstuminers.hackathon2.model.DatasetInfo;
import org.bmstuminers.hackathon2.model.DatasetLine;
import org.bmstuminers.hackathon2.repo.DatasetLineRepository;
import org.bmstuminers.hackathon2.repo.DatasetRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.security.SecureRandom;
import java.util.*;

/**
 * Service for datasets
 */
@Service
public class DatasetService {

    @Autowired
    private DatasetRepository repository;
    @Autowired
    private DatasetLineRepository lineRepository;

    public DatasetService() {

    }

    /**
     * Creates a new dataset
     * @param info dataset info
     * @return save dataset
     */
    public DatasetInfo create(DatasetInfo info) {
        info.setId(new ObjectId().toString());
        return repository.save(info);
    }


    /**
     * Gets dataset
     * @param id dataset id
     * @return dataset
     */
    public DatasetInfo findOne(String id) {
        DatasetInfo info = repository.findOne(id);
        if (info == null) {
            throw new IllegalArgumentException("Wrong dataset id");
        }
        return info;
    }

    /**
     * Gets list of all dataset categories
     * @return categories
     */
    public Collection<String> getCategories() {
        Set<String> categories = new TreeSet<>();
        for (DatasetInfo info: repository.findAll()) {
            categories.add(info.getCategory());
        }
        return categories;
    }

    /**
     * Deletes dataset
     * @param id dataset id
     */
    public void delete(String id) {
        repository.delete(id);
    }

    /**
     * Gets all datasets filtered by category in info
     * @param info dataset category container
     *             if null - finds everything
     * @return filtered datasets
     */
    public Iterable<DatasetInfo> findByCategory(DatasetInfo info) {
        if (info.getCategory() == null) {
            return repository.findAll();
        } else {
            return repository.findByCategory(info.getCategory());
        }
    }

    /**
     * Adds a new line into dataset
     * @param datasetId id of the dataset
     * @param data line data
     * @return dataset line
     */
    public DatasetLine addLine(String datasetId, List<String> data) {
        DatasetInfo info = repository.findOne(datasetId);
        if (info == null) {
            throw new IllegalArgumentException("Wrong dataset id");
        }
        if (info.getFields().size() != data.size()) {
            throw  new IllegalArgumentException("Expected " + info.getFields().size() +
             " fields, but got " + data.size());
        }
        DatasetLine line = new DatasetLine();
        line.setId(new ObjectId().toString());
        line.setDatasetId(datasetId);
        line.setData(data);
        return lineRepository.save(line);
    }

    /**
     * Deletes the given line
     * @param lineId id of the line
     */
    public void deleteLine(String lineId) {
        lineRepository.delete(lineId);
    }

    /**
     * Gets dataset data
     * @param id dataset id
     * @return dataset
     */
    public Dataset getData(String id) {
        DatasetInfo info = repository.findOne(id);
        if (info == null) {
            throw new IllegalArgumentException("Wrong dataset id");
        }
        Dataset dataset = new Dataset();
        dataset.setFields(info.getFields());
        List<DatasetLine> lines = lineRepository.findByDatasetId(id);
        List<List<String>> page = new ArrayList<>();
        for (DatasetLine line: lines) {
            page.add(line.getData());
        }
        dataset.setPage(page);
        return dataset;
    }

    public DatasetInfo update(String id, DatasetInfo info) {
        info.setId(id);
        return repository.save(info);
    }
}
