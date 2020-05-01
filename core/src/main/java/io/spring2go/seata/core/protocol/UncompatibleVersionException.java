package io.spring2go.seata.core.protocol;

/**
 * Created by william on May, 2020
 */
public class UncompatibleVersionException extends Exception {
    public UncompatibleVersionException(String message) {
        super(message);
    }
}
