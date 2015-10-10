package org.bmstuminers.hackathon2.repo;

import org.bmstuminers.hackathon2.model.Chain;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository for chains
 * @author Konstantin Grechishchev
 */
public interface ChainRepository extends MongoRepository<Chain, String> {
}
