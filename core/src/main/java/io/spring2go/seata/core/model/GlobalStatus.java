package io.spring2go.seata.core.model;

/**
 * Created by william on May, 2020
 *
 * Status of global transaction
 */
public enum GlobalStatus {
    // Unknown
    UnKnown,

    // PHASE 1: can accept new branch registering.
    Begin,


    /** PHASE 2: Running Status: may be changed any time. */

    // Committing.
    Committing,

    // Retrying commit after a recoverable failure.
    CommitRetrying,

    // Rollbacking
    Rollbacking,

    // Retrying rollback after a recoverable failure.
    RollbackRetrying,

    // Rollbacking since timeout
    TimeoutRollbacking,

    // Retrying rollback (since timeout) after a recoverable failure.
    TimeoutRollbackRetrying,


    /** PHASE 2: Final Status: will NOT change any more. */

    // Finally: global transaction is successfully committed.
    Committed,

    // Finally: failed to commit
    CommitFailed,

    // Finally: global transaction is successfully rollbacked.
    Rollbacked,

    // Finally: failed to rollback
    RollbackFailed,

    // Finally: global transaction is successfully rollbacked since timeout.
    TimeoutRollbacked,

    // Finally: failed to rollback since timeout
    TimeoutRollbackFailed,

    // Not managed in session map any more
    Finished;

    public static GlobalStatus get(byte ordinal) {
        return get((int) ordinal);
    }

    public static GlobalStatus get(int ordinal) {
        for (GlobalStatus globalStatus : GlobalStatus.values()) {
            if (globalStatus.ordinal() == ordinal) {
                return globalStatus;
            }
        }
        throw new IllegalArgumentException("Unknown GlobalStatus[" + ordinal + "]");
    }
}
