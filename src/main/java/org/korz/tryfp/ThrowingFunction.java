package org.korz.tryfp;

@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception> {
    R apply(T value) throws E;
}
