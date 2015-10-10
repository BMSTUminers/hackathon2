package org.bmstuminers.hackathon2.web;

import org.bmstuminers.hackathon2.exception.ProcessingException;
import org.bmstuminers.hackathon2.exception.WrongParameterException;
import org.bmstuminers.hackathon2.model.BlockInfo;
import org.bmstuminers.hackathon2.model.Chain;
import org.bmstuminers.hackathon2.model.Dataset;
import org.bmstuminers.hackathon2.model.DatasetInfo;
import org.bmstuminers.hackathon2.service.ChainService;
import org.bmstuminers.hackathon2.service.block.Block;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Chain create(@RequestBody List<BlockInfo> blocks) throws WrongParameterException {
        return this.chainService.create(blocks);
    }

    /**
     * Gets all chains
     */
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public Iterable<Chain> findAll() {
        return this.chainService.findAll();
    }

    /**
     * Deletes chain
     * @param id chain id
     */
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public void delete(@PathVariable String id) {
        this.chainService.delete(id);
    }

    /**
     * Updates the given chain
     * @param blocks blocks info
     * @return saved chain
     */
    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST, produces = "application/json")
    public Chain execute(@PathVariable String id,
                         @RequestBody List<BlockInfo> blocks) throws WrongParameterException, ProcessingException {
        return this.chainService.update(id, blocks);
    }

    /**
     * Executes chain
     * @param id chain id
     */
    @RequestMapping(value = "/{id}/execute", method = RequestMethod.GET, produces = "application/json")
    public Dataset execute(@PathVariable String id,
                           @RequestParam(required = false) Integer page,
                           @RequestParam(required = false) Integer pageSize)
            throws WrongParameterException, ProcessingException {

        if (page == null) page = 0;
        if (pageSize == null) pageSize = Integer.MAX_VALUE;
        return this.chainService.execute(id, page, pageSize);
    }
}
