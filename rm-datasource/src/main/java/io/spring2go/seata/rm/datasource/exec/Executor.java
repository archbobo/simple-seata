package io.spring2go.seata.rm.datasource.exec;

/**
 * Created by william on May, 2020
 */
public interface Executor<T> {
    T execute(Object... args) throws Throwable;
}
