package io.spring2go.seata.core.model;

import io.spring2go.seata.core.exception.TransactionException;

/**
 * Created by william on May, 2020
 *
 * Resource Manager: receive inbound request from TC.
 */
public interface ResourceManagerInbound {
    /**
     * Commit a branch transaction.
     *
     * @param xid Transaction id.
     * @param branchId Branch id.
     * @param resourceId Resource id.
     * @param applicationData Application data bind with this branch.
     * @return Status of the branch after committing.
     * @throws TransactionException Any exception that fails this will be wrapped with TransactionException and thrown out.
     */
    BranchStatus branchCommit(String xid, long branchId, String resourceId, String applicationData) throws TransactionException;


    /**
     * Rollback a branch transaction.
     *
     * @param xid Transaction id.
     * @param branchId Branch id.
     * @param resourceId Resource id.
     * @param applicationData Application data bind with this branch.
     * @return Status of the branch after rollbacking.
     * @throws TransactionException Any exception that fails this will be wrapped with TransactionException and thrown out.
     */
    BranchStatus branchRollback(String xid, long branchId, String resourceId, String applicationData) throws TransactionException;
}
