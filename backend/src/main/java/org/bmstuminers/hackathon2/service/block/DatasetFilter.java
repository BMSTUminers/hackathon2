package org.bmstuminers.hackathon2.service.block;

import org.bmstuminers.hackathon2.exception.ProcessingException;
import org.bmstuminers.hackathon2.model.Dataset;
import org.bmstuminers.hackathon2.model.ParameterDescription;
import org.bmstuminers.hackathon2.service.block.predicates.ComparePredicate;
import org.bmstuminers.hackathon2.service.block.predicates.FilterPredicate;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Universal filter block
 * 
 * @author rssdev10
 */
public abstract class DatasetFilter extends AbstractBlock {

    private final static List<ParameterDescription> parameterDescriptions = new ArrayList<>();
    private final Pattern p = Pattern.compile("([^\\d\\._]*)([\\d\\.]+)");

    private static final Map<String, ComparePredicate<Integer>> intPredicateMap =
            new HashMap<String, ComparePredicate<Integer>>() {
                {
                    put(">", (cur, other) -> cur > other);
                    put(">=", (cur, other) -> cur >= other);
                    put("<", (cur, other) -> cur < other);
                    put("<=", (cur, other) -> cur <= other);
                    put("=", Integer::equals);
                }
            };

    private static final Map<String, ComparePredicate<Double>> doublePredicateMap =
            new HashMap<String, ComparePredicate<Double>>() {
                {
                    put(">", (cur, other) -> cur > other);
                    put(">=", (cur, other) -> cur >= other);
                    put("<", (cur, other) -> cur < other);
                    put("<=", (cur, other) -> cur <= other);
                    put("=", Double::equals);
                }
            };

    public DatasetFilter(String name, String description) {
        super(name, description);
    }

	@Override
	protected Dataset doProcess(Dataset input, Map<String, String> params)
			throws ProcessingException {

        List<FilterPredicate> predicates = parseFilterString(params);
        Predicate<List<String>> unitedPredicate = union(predicates);

		Dataset filtered = new Dataset();
		filtered.setFields(input.getFields());
        List<List<String>> filteredPage = new ArrayList<>();
        for (List<String> row: input.getPage()) {
            if (unitedPredicate.test(row)) {
                filteredPage.add(row);
            }
        }
		filtered.setPage(filteredPage);

		return filtered;
	}

    protected abstract Predicate<List<String>> union(List<FilterPredicate> predicates);

	@Override
	public List<ParameterDescription> getParamsDescription() {
		return parameterDescriptions;
	}

	private List<FilterPredicate> parseFilterString(Map<String, String> params) {
        List<FilterPredicate> predicates = new ArrayList<>();
        for (Map.Entry<String, String> en: params.entrySet()) {
            String columnAlias = en.getKey();
            try {
                String colStr = columnAlias.replaceFirst("_+", "");
                int columnId = Integer.valueOf(colStr);
                String[] data = parseEquation(en.getValue());
                String value = data[1];
                if (value.contains(".")) { //Double
                    ComparePredicate<Double> p = doublePredicateMap.get(data[0]);
                    if (p == null) {
                        throw new IllegalArgumentException("Wrong operation: " + data[0]);
                    }
                    predicates.add(new FilterPredicate<>(columnId, value, Double::valueOf, p));
                } else {
                    ComparePredicate<Integer> p = intPredicateMap.get(data[0]);
                    if (p == null) {
                        throw new IllegalArgumentException("Wrong operation: " + data[0]);
                    }
                    predicates.add(new FilterPredicate<>(columnId, value, Integer::valueOf, p));
                }
            } catch (NumberFormatException ignored) { } //just ignore non number parameters
        }
        return predicates;
	}

    private String[] parseEquation(String equation) {
        String[] res = new String[2];
        Matcher matcher = p.matcher(equation);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Wrong equation: " + equation);
        }
        res[0] = matcher.group(1);
        res[1] = matcher.group(2);
        return res;
    }
}
