package org.bmstuminers.hackathon2.service.block.predicates;

/**
 * @author Konstantin Grechishchev
 */
public interface ComparePredicate<T> {

    boolean test(T current, T comparedValue);
}
