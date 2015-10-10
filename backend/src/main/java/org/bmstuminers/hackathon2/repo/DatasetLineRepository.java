package org.bmstuminers.hackathon2.repo;

import org.bmstuminers.hackathon2.model.DatasetLine;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository for dataset lines
 * @author Konstantin Grechishchev
 */
public interface DatasetLineRepository extends MongoRepository<DatasetLine, String> {

    /**
     * Gets lines by the given dataset id
     * @param id id of the dataset
     * @return lines
     */
    List<DatasetLine> findByDatasetId(String id);

}