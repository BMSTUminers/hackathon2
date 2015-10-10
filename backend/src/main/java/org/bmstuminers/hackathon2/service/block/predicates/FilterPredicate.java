package org.bmstuminers.hackathon2.service.block.predicates;

import java.util.List;
import java.util.function.Predicate;

/**
 * Base filter predicate
 * @author Konstantin Grechishchev
 */
public class FilterPredicate<T> implements Predicate<List<String>> {

    private int column;
    private T compareToValue;
    private ValueConverter<T, String> converter;
    private ComparePredicate<T> primitivePredicate;


    public FilterPredicate(ValueConverter<T, String> converter, ComparePredicate<T> primitivePredicate) {
        this.converter = converter;
        this.primitivePredicate = primitivePredicate;
    }

    public FilterPredicate(int column, String compareToValue,
                           ValueConverter<T, String> converter,
                           ComparePredicate<T> primitivePredicate) {
        this.converter = converter;
        this.primitivePredicate = primitivePredicate;
        this.column = column;
        this.compareToValue = this.converter.convert(compareToValue);
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public T getCompareToValue() {return compareToValue;}

    public void setCompareToValue(T compareToValue) {
        this.compareToValue = compareToValue;
    }

    public ValueConverter<T, String> getConverter() {
        return converter;
    }

    public void setConverter(ValueConverter<T, String> converter) {
        this.converter = converter;
    }

    @Override
    public boolean test(List<String> row) {
        T current = converter.convert(row.get(column));
        return primitivePredicate.test(current, compareToValue);
    }

}