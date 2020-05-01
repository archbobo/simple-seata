package io.spring2go.seata.core.protocol.transaction;

import io.spring2go.seata.core.rpc.RpcContext;

/**
 * Created by william on May, 2020
 */
public interface TCInboundHandler {
    GlobalBeginResponse handle(GlobalBeginRequest globalBegin, RpcContext rpcContext);

    GlobalCommitResponse handle(GlobalCommitRequest globalCommit, RpcContext rpcContext);

    GlobalRollbackResponse handle(GlobalRollbackRequest globalRollback, RpcContext rpcContext);

    BranchRegisterResponse handle(BranchRegisterRequest branchRegister, RpcContext rpcContext);

    BranchReportResponse handle(BranchReportRequest branchReport, RpcContext rpcContext);

    GlobalLockQueryResponse handle(GlobalLockQueryRequest checkLock, RpcContext rpcContext);

    GlobalStatusResponse handle(GlobalStatusRequest globalStatus, RpcContext rpcContext);
}
