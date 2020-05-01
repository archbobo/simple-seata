package io.spring2go.seata.core.model;

import io.spring2go.seata.common.exception.ShouldNeverHappenException;

/**
 * Created by william on May, 2020
 *
 * Status of branch transaction
 */
public enum BranchStatus {
    // Unknown
    Unknown,

    // Registered to TC.
    Registered,

    // Branch logic is successfully done at phase one.
    PhaseOne_Done,

    // Branch logic is failed at phase one.
    PhaseOne_Failed,

    // Branch logic is NOT reported for a timeout.
    PhaseOne_Timeout,

    // Commit logic is successfully done at phase two.
    PhaseTwo_Committed,

    // Commit logic is failed but retriable.
    PhaseTwo_CommitFailed_Retriable,

    // Commit logic is failed and NOT retriable.
    PhaseTwo_CommitFailed_Unretriable,

    // Rollback logic is successfully done at phase two.
    PhaseTwo_Rollbacked,

    // Rollback logic is failed but retriable.
    PhaseTwo_RollbackFailed_Retriable,

    // Rollback logic is failed but NOT retriable.
    PhaseTwo_RollbackFailed_Unretriable;

    public static BranchStatus get(byte ordinal) {
        return get((int) ordinal);
    }

    public static BranchStatus get(int ordinal) {
        for (BranchStatus branchStatus : BranchStatus.values()) {
            if (branchStatus.ordinal() == ordinal) {
                return branchStatus;
            }
        }
        throw new ShouldNeverHappenException("Unknown BranchStatus[" + ordinal + "]");
    }
}
