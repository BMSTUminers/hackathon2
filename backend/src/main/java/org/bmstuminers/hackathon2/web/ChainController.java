package org.bmstuminers.hackathon2.web;

import org.bmstuminers.hackathon2.exception.WrongParameterException;
import org.bmstuminers.hackathon2.model.BlockInfo;
import org.bmstuminers.hackathon2.model.Chain;
import org.bmstuminers.hackathon2.model.DatasetInfo;
import org.bmstuminers.hackathon2.service.ChainService;
import org.bmstuminers.hackathon2.service.block.Block;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

/**
 * @author Konstantin Grechishchev
 */
@RestController
@RequestMapping({"/api/chain"})
public class ChainController {

    @Autowired
    private ChainService chainService;

    /**
     * Gets list of all dataset categories
     * @return categories
     */
    @RequestMapping(value = "/blocks", method = RequestMethod.GET, produces = "application/json")
    public List<Block> getCategories() {
        return this.chainService.getAvailableBlocks();
    }

    /**
     * Creates a new chain
     * @param blocks blocks info
     * @return saved chain
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public Chain create(@RequestBody List<BlockInfo> blocks) throws WrongParameterException{
        return this.chainService.create(blocks);
    }
}
