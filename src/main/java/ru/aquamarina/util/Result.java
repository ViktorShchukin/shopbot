package ru.aquamarina.util;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public sealed interface Result<R,E> permits ResultOk, ResultError {

    static <R, E> Result<R,E> ok(R result) {
        Objects.requireNonNull(result);
        return new ResultOk(result);
    }

    static <R, E> Result<R, E> error(E error) {
        Objects.requireNonNull(error);
        return new ResultError(error);
    }

    Optional<R> ok();

    Optional<E> error();

    <U> Result<U, E> map(Function<? super R, Result<U, E>> mapper);

    // todo think about it
    <U> Result<U, E> mapValue(Function<? super R, U> mapper);

    <G> Result<R,G> or(Function<? super E, Result<R, G>> mapper);

    R recover(Function<? super E, R> mapper);

    R unwrap();
    void expect(String msg);
}
