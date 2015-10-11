package org.bmstuminers.hackathon2.service.block.predicates;

/**
 * Interface to convert values for filters
 * @author Konstantin Grechishchev
 */
public interface ValueConverter<T,V> {

    T convert(V value);
}
