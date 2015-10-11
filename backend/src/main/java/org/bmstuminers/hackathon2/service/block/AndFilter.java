package org.bmstuminers.hackathon2.service.block;

import org.bmstuminers.hackathon2.service.block.predicates.FilterPredicate;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author Konstantin Grechishchev
 */
public class AndFilter extends DatasetFilter {

    private final static String NAME = "andFilter";
    private final static String  DESCRIPTION= "Filter provided dataset";

    public AndFilter() {
        super(NAME, DESCRIPTION);
    }

    @Override
    protected Predicate<List<String>> union(List<FilterPredicate> predicates) {
        Predicate<List<String>> predicate = strings -> true;
        for (FilterPredicate filterPredicate: predicates) {
            predicate = predicate.and(filterPredicate);
        }
        return predicate;
    }
}