package io.spring2go.seata.core.context;

/**
 * Created by william on May, 2020
 */
public interface ContextCore {
    String put(String key, String value);

    String get(String key);

    String remove(String key);
}
