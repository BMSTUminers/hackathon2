package org.bmstuminers.hackathon2.service.block;

import org.bmstuminers.hackathon2.exception.ProcessingException;
import org.bmstuminers.hackathon2.exception.WrongParameterException;
import org.bmstuminers.hackathon2.model.Dataset;
import org.bmstuminers.hackathon2.model.ParameterDescription;

import java.util.List;
import java.util.Map;

/**
 * Base interface for chain element (block)
 * @author Konstantin Grechishchev
 */
public interface Block {

    /**
     * Process the given input dataSet
     * @param input input data set
     * @param params algorithm parameters
     * @return processed dataset
     * @throws WrongParameterException if wrong parameters map is given
     * @throws ProcessingException in case of any error happened during processing
     */
    Dataset process(Dataset input, Map<String, String> params)
            throws WrongParameterException, ProcessingException;

    /**
     * Method checks parameter map to make sure all required params are present.
     * For missed optional params method should add the default values into the map
     * @param params params map
     * @throws WrongParameterException if wrong parameters map is given
     */
    void checkParams(Map<String, String> params) throws WrongParameterException;

    /**
     * @return block name. Will be used in api to construct the chain
     */
    String getName();

    /**
     * @return block plain text description
     */
    String getDescription();

    /**
     * @return information regarding block parameters. These params will be used in api to construct the chain
     */
    List<ParameterDescription> getParamsDescription();
}
