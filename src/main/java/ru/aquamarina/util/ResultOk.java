package ru.aquamarina.util;

import java.util.Optional;
import java.util.function.Function;

public record ResultOk<R,E>(R result) implements Result<R,E> {

    @Override
    public Optional<R> ok() {
        return Optional.of(result);
    }

    @Override
    public Optional<E> error() {
        return Optional.empty();
    }

    @Override
    public <U> Result<U, E> map(Function<? super R, Result<U, E>> mapper) {
        return mapper.apply(result);
    }

    @Override
    public <G> Result<R,G> or(Function<? super E, Result<R, G>> mapper) {
        return Result.ok(result);
    }

    public R recover(Function<? super E, R> mapper) {
        return result;
    }

    @Override
    public R unwrap() {
        return result;
    }

    @Override
    public void expect(String msg) {

    }

    @Override
    public String toString() {
        return result.toString();
    }
}
