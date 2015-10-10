package org.bmstuminers.hackathon2.repo;

import org.bmstuminers.hackathon2.model.CachedDataset;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Konstantin Grechishchev
 */
public interface CachedDatasetRepository extends MongoRepository<CachedDataset, String> {

}
