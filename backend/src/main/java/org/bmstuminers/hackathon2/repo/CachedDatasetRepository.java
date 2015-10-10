package org.bmstuminers.hackathon2.repo;

import org.bmstuminers.hackathon2.model.CachedDatasetInfo;
import org.bmstuminers.hackathon2.model.CachedDatasetLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Konstantin Grechishchev
 */
public interface CachedDatasetRepository extends MongoRepository<CachedDatasetInfo, String> {

    List<CachedDatasetInfo> removeByChainId(String chainId);

    CachedDatasetInfo findOneByChainId(String chainId);
}
