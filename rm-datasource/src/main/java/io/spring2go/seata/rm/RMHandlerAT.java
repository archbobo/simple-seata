package io.spring2go.seata.rm;

import io.spring2go.seata.core.exception.TransactionException;
import io.spring2go.seata.core.model.BranchStatus;
import io.spring2go.seata.core.protocol.transaction.*;
import io.spring2go.seata.core.rpc.TransactionMessageHandler;
import io.spring2go.seata.rm.datasource.DataSourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by william on May, 2020
 */
public class RMHandlerAT extends AbstractRMHandlerAT implements RMInboundHandler, TransactionMessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RMHandlerAT.class);

    private DataSourceManager dataSourceManager = DataSourceManager.get();

    @Override
    protected void doBranchCommit(BranchCommitRequest request, BranchCommitResponse response) throws TransactionException {
        String xid = request.getXid();
        long branchId = request.getBranchId();
        String resourceId = request.getResourceId();
        String applicationData = request.getApplicationData();
        LOGGER.info("AT Branch committing: " + xid + " " + branchId + " " + resourceId + " " + applicationData);
        BranchStatus status = dataSourceManager.branchCommit(xid, branchId, resourceId, applicationData);
        response.setBranchStatus(status);
        LOGGER.info("AT Branch commit result: " + status);

    }

    @Override
    protected void doBranchRollback(BranchRollbackRequest request, BranchRollbackResponse response) throws TransactionException {
        String xid = request.getXid();
        long branchId = request.getBranchId();
        String resourceId = request.getResourceId();
        String applicationData = request.getApplicationData();
        LOGGER.info("AT Branch rolling back: " + xid + " " + branchId + " " + resourceId);
        BranchStatus status = dataSourceManager.branchRollback(xid, branchId, resourceId, applicationData);
        response.setBranchStatus(status);
        LOGGER.info("AT Branch rollback result: " + status);

    }
}
