package org.korz.tryfp;

@FunctionalInterface
public interface ThrowingSupplier<R, E extends Exception> {
    R get() throws E;
}
