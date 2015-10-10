package org.bmstuminers.hackathon2.service.block;

import org.bmstuminers.hackathon2.exception.ProcessingException;
import org.bmstuminers.hackathon2.exception.WrongParameterException;
import org.bmstuminers.hackathon2.model.Dataset;
import org.bmstuminers.hackathon2.model.ParameterDescription;

import java.util.Map;

/**
 * Base class for chain element (block)
 * @author Konstantin Grechishchev
 */
public abstract class AbstractBlock implements Block {

    /**
     * Name (id) of the block. Will be used in api to construct the chain
     */
    private String name;
    private String description;

    public AbstractBlock(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() { return name; }
    @Override
    public String getDescription() { return description; }

    /**
     * Process the given input dataSet
     * @param input input data set. Can be null if this block is first in the chain
     * @param params algorithm parameters
     * @return processed dataset
     * @throws WrongParameterException if wrong parameters map is given
     * @throws ProcessingException in case of any error happened during processing
     */
    @Override
    public Dataset process(Dataset input, Map<String, String> params)
            throws WrongParameterException, ProcessingException {
        checkParams(params);
        return doProcess(input, params);
    }

    /**
     * Method check parameter map to make sure all required params are present.
     * For missed optional params method should add the default values into the map
     * @param params params map
     * @throws WrongParameterException if wrong parameters map is given
     */
    @Override
    public void checkParams(Map<String, String> params) throws WrongParameterException {
        for (ParameterDescription description: getParamsDescription()) {
            if (!params.containsKey(description.getName())) {
                if (description.isOptional()) {
                    params.put(description.getName(), description.getDefaultValue());
                } else {
                    throw new WrongParameterException("Missing required parameter: " + description.getName());
                }
            }
        }
    }

    //============= METHOD TO BE IMPLEMENTED IN CHILD CLASSES =======================================//

    /**
     * Method performs transformation of the given dataset and  returns the output (processed dataset)
     * Params map is already validated by check params method.
     * @param input input dataset. Can be null if this block is first in the chain
     * @param params parameters map
     * @return output dataset
     * @throws ProcessingException in case of any error happened during processing
     */
    protected abstract Dataset doProcess(Dataset input, Map<String, String> params) throws ProcessingException;




}
