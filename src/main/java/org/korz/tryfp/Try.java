package org.korz.tryfp;

public interface Try<T> {
    static <T> Try<T> ofValue(T value) {
        return new Success<>(value);
    }

    static <T> Try<T> ofError(Exception err) {
        return new Failure<>(err);
    }

    static Try<Void> unit() {
        return ofValue(null);
    }

    static <T> Try<T> of(ThrowingSupplier<T, ?> f) {
        return unit().map(nil -> f.get());
    }

    T getValue() throws Exception;
    <R> Try<R> map(ThrowingFunction<? super T, R, ?> f);
    <R> Try<R> flatMap(ThrowingFunction<? super T, Try<R>, ?> f);
}

class Success<T> implements Try<T> {

    private final T value;

    Success(T value) {
        this.value = value;
    }

    @Override // Try
    public T getValue() {
        return value;
    }

    @Override // Try
    public <R> Try<R> map(ThrowingFunction<? super T, R, ?> f) {
        try {
            return new Success<>(f.apply(value));
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }

    @Override // Try
    public <R> Try<R> flatMap(ThrowingFunction<? super T, Try<R>, ?> f) {
        try {
            return f.apply(value);
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }
}

class Failure<T> implements Try<T> {

    private final Exception error;

    Failure(Exception error) {
        this.error = error;
    }

    @Override // Try
    public T getValue() throws Exception {
        throw error;
    }

    @SuppressWarnings("unchecked") // T is unused, so it's safe to cast it arbitrarily
    private <R> Try<R> self() {
        return (Failure<R>) this;
    }

    @Override // Try
    public <R> Try<R> map(ThrowingFunction<? super T, R, ?> f) {
        return self();
    }

    @Override // Try
    public <R> Try<R> flatMap(ThrowingFunction<? super T, Try<R>, ?> f) {
        return self();
    }
}
