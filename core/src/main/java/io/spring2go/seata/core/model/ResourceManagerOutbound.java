package io.spring2go.seata.core.model;

import io.spring2go.seata.core.exception.TransactionException;

/**
 * Created by william on May, 2020
 *
 * Resource Manager: send outbound request to TC
 */
public interface ResourceManagerOutbound {
    Long branchRegister(BranchType branchType, String resourceId, String clientId, String xid, String lockKeys) throws
            TransactionException;

    void branchReport(String xid, long branchId, BranchStatus status, String applicationData) throws TransactionException;

    boolean lockQuery(BranchType branchType, String resourceId, String xid, String lockKeys) throws TransactionException;
}
