package org.bmstuminers.hackathon2.service.block;

import org.bmstuminers.hackathon2.exception.ProcessingException;
import org.bmstuminers.hackathon2.model.Dataset;
import org.bmstuminers.hackathon2.model.ParameterDescription;
import org.bmstuminers.hackathon2.service.DatasetService;

import java.util.*;
import java.util.function.Predicate;

/**
 * Universal filter block
 * 
 * @author rssdev10
 */
public class DatasetFilter extends AbstractBlock {

	private final static String NAME = "filter";
	private final static String DESCRIPTION = "Filter provided dataset";

	private final static List<ParameterDescription> parameterDescriptions = new ArrayList<>();
	private final DatasetService datasetService;

	private class FilterValue {
		public String string;
		public String num;
		public String date;
	}

	public DatasetFilter(DatasetService datasetService) {
		super(NAME, DESCRIPTION);
		this.datasetService = datasetService;
	}

	@Override
	protected Dataset doProcess(Dataset input, Map<String, String> params)
			throws ProcessingException {

		Map<String, Predicate<FilterValue>> ops = parseFilterString(params);
		
		Dataset filtered = new Dataset();
		filtered.setFields(input.getFields());
		filtered.setPage(input.getPage());

		return filtered;
	}

	@Override
	public List<ParameterDescription> getParamsDescription() {
		return parameterDescriptions;
	}

	Map<String, Predicate<FilterValue>> parseFilterString(Map<String, String> params) {
		return null;
	}
}
