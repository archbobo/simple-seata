package io.spring2go.seata.core.exception;

/**
 * Created by william on May, 2020
 */
public class TransactionException extends Exception {
    protected TransactionExceptionCode code = TransactionExceptionCode.Unknown;

    public TransactionExceptionCode getCode() {
        return code;
    }

    public TransactionException(TransactionExceptionCode code) {
        this.code = code;
    }
    public TransactionException(TransactionExceptionCode code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(TransactionExceptionCode code, String message) {
        super(message);
        this.code = code;
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionException(TransactionExceptionCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
