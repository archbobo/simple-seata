package io.spring2go.seata.core.protocol.transaction;

/**
 * Created by william on May, 2020
 */
public interface RMInboundHandler {
    BranchCommitResponse handle(BranchCommitRequest request);

    BranchRollbackResponse handle(BranchRollbackRequest request);
}
