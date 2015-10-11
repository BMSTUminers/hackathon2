package org.bmstuminers.hackathon2.service.block;

import org.bmstuminers.hackathon2.model.Dataset;
import org.bmstuminers.hackathon2.service.block.DatasetFilter;
import org.bmstuminers.hackathon2.service.block.predicates.FilterPredicate;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author Konstantin Grechishchev
 */
public class OrFilter extends DatasetFilter {

    private final static String NAME = "orFilter";
    private final static String  DESCRIPTION= "Filter provided dataset";

    public OrFilter() {
        super(NAME, DESCRIPTION);
    }

    @Override
    protected Predicate<List<String>> union(List<FilterPredicate> predicates) {
        if (predicates.size() == 0) return strings -> true;

        Predicate<List<String>> predicate = strings -> false;
        for (FilterPredicate filterPredicate: predicates) {
            predicate = predicate.or(filterPredicate);
        }
        return predicate;
    }
}
