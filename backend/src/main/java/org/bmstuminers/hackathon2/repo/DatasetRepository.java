package org.bmstuminers.hackathon2.repo;

import org.bmstuminers.hackathon2.model.DatasetInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repo for dataset operations
 */
public interface DatasetRepository extends MongoRepository<DatasetInfo, String> {

    List<DatasetInfo> findByCategory(String category);

}