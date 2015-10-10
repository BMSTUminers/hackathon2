package org.bmstuminers.hackathon2.repo;

import org.bmstuminers.hackathon2.model.CachedDatasetLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository for caching of the dataset results
 * @author Konstantin Grechishchev
 */
public interface CachedDatasetLineRepository extends MongoRepository<CachedDatasetLine, String>  {

    List<CachedDatasetLine> removeByChainId(String chainId);

    Page<CachedDatasetLine> findByChainId(String chainId, Pageable pageable);

    List<CachedDatasetLine> findByChainId(String chainId);

}
