package io.spring2go.seata.core.exception;

/**
 * Created by william on May, 2020
 */
public enum TransactionExceptionCode {
    //
    Unknown,

    //
    LockKeyConflict,

    //
    IO,

    //
    BranchRollbackFailed_Retriable,

    //
    BranchRollbackFailed_Unretriable,

    //
    BranchRegisterFailed,

    //
    BranchReportFailed,

    //
    LockableCheckFailed,

    //
    BranchTransactionNotExist,

    //
    GlobalTransactionNotExist,

    //
    GlobalTransactionNotActive,

    //
    GlobalTransactionStatusInvalid,

    //
    FailedToSendBranchCommitRequest,

    //
    FailedToSendBranchRollbackRequest,

    //
    FailedToAddBranch,


    ;


    public static TransactionExceptionCode get(byte ordinal) {
        return get((int) ordinal);
    }
    public static TransactionExceptionCode get(int ordinal) {
        for (TransactionExceptionCode value : TransactionExceptionCode.values()) {
            if (value.ordinal() == ordinal) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown TransactionExceptionCode[" + ordinal + "]");
    }
}
