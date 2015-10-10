package org.bmstuminers.hackathon2.web;

import org.bmstuminers.hackathon2.service.ChainService;
import org.bmstuminers.hackathon2.service.block.Block;
import org.springframework.beans.factory.annotation.Autowired;
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
}
