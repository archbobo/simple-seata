package io.spring2go.seata.common.exception;

public class NotSupportYetException extends RuntimeException {
    public NotSupportYetException() {
        super();
    }

    public NotSupportYetException(String message) {
        super(message);
    }

    public NotSupportYetException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSupportYetException(Throwable cause) {
        super(cause);
    }
}
