package org.bmstuminers.hackathon2.web;

import org.bmstuminers.hackathon2.model.Dataset;
import org.bmstuminers.hackathon2.model.DatasetInfo;
import org.bmstuminers.hackathon2.model.DatasetLine;
import org.bmstuminers.hackathon2.service.DatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping({"/api/dataset"})
public class DatasetController {

    @Autowired
    private DatasetService datasetService;

    public DatasetController() { }

    /**
     * Creates a new dataset
     * @param info dataset info
     * @return saved dataset
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public DatasetInfo create(@RequestBody DatasetInfo info) {
        return this.datasetService.create(info);
    }

    /**
     * Gets dataset
     * @param id dataset id
     * @return dataset
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public DatasetInfo get(@PathVariable String id) {
        return this.datasetService.findOne(id);
    }

    /**
     * Creates a new dataset
     * @param info dataset info
     * @return saved dataset
     */
    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST, produces = "application/json")
    public DatasetInfo update(@PathVariable String id,
                              @RequestBody DatasetInfo info) {
        return this.datasetService.update(id, info);
    }

    /**
     * Gets list of all dataset categories
     * @return categories
     */
    @RequestMapping(value = "/categories", method = RequestMethod.GET, produces = "application/json")
    public Collection<String> getCategories() {
        return this.datasetService.getCategories();
    }


    /**
     * Deletes dataset
     * @param id dataset id
     */
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public void delete(@PathVariable String id) {
        this.datasetService.delete(id);
    }

    /**
     * Gets all datasets filtered by category in info
     * @param info dataset category container
     *             if null - finds everything
     * @return filtered datasets
     */
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json")
    public Iterable<DatasetInfo> findByCategory(@RequestBody DatasetInfo info) {
        return this.datasetService.findByCategory(info);
    }


    /**
     * Adds a new line into dataset
     * @param datasetId id of the dataset
     * @param data line data
     * @return dataset line
     */
    @RequestMapping(value = "{datasetId}/line/add", method = RequestMethod.POST, produces = "application/json")
    public DatasetLine findByCategory(@PathVariable String datasetId,
                                      @RequestBody List<String> data) {
        return this.datasetService.addLine(datasetId, data);
    }

    /**
     * Deletes the given line
     * @param lineId id of the line
     */
    @RequestMapping(value = "/line/{lineId}/delete", method = RequestMethod.GET)
    public void deleteLine(@PathVariable String lineId) {
        this.datasetService.deleteLine(lineId);
    }

    /**
     * Gets dataset data
     * @param id dataset id
     * @return dataset
     */
    @RequestMapping(value = "/{id}/data", method = RequestMethod.GET, produces = "application/json")
    public Dataset getData(@PathVariable String id) {
        return this.datasetService.getData(id);
    }
    



}