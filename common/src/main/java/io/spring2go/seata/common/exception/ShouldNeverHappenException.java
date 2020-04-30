package io.spring2go.seata.common.exception;

public class ShouldNeverHappenException extends RuntimeException {
    public ShouldNeverHappenException() {
        super();
    }

    public ShouldNeverHappenException(String message) {
        super(message);
    }

    public ShouldNeverHappenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShouldNeverHappenException(Throwable cause) {
        super(cause);
    }
}
