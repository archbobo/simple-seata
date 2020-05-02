package io.spring2go.seata.rm;

import io.spring2go.seata.common.exception.ShouldNeverHappenException;
import io.spring2go.seata.core.exception.AbstractExceptionHandler;
import io.spring2go.seata.core.exception.TransactionException;
import io.spring2go.seata.core.protocol.AbstractMessage;
import io.spring2go.seata.core.protocol.AbstractResultMessage;
import io.spring2go.seata.core.protocol.transaction.*;
import io.spring2go.seata.core.rpc.RpcContext;
import io.spring2go.seata.core.rpc.TransactionMessageHandler;

/**
 * Created by william on May, 2020
 */
public abstract class AbstractRMHandlerAT extends AbstractExceptionHandler
    implements RMInboundHandler, TransactionMessageHandler {
    @Override
    public BranchCommitResponse handle(BranchCommitRequest request) {
        BranchCommitResponse response = new BranchCommitResponse();
        exceptionHandleTemplate(new Callback<BranchCommitRequest, BranchCommitResponse>() {
            @Override
            public void execute(BranchCommitRequest request, BranchCommitResponse response) throws TransactionException {
                doBranchCommit(request, response);
            }
        }, request, response);
        return response;
    }

    protected abstract void doBranchCommit(BranchCommitRequest request, BranchCommitResponse response) throws TransactionException;

    @Override
    public BranchRollbackResponse handle(BranchRollbackRequest request) {
        BranchRollbackResponse response = new BranchRollbackResponse();
        exceptionHandleTemplate(new Callback<BranchRollbackRequest, BranchRollbackResponse>() {
            @Override
            public void execute(BranchRollbackRequest request, BranchRollbackResponse response) throws TransactionException {
                doBranchRollback(request, response);
            }
        }, request, response);
        return response;
    }

    protected abstract void doBranchRollback(BranchRollbackRequest request, BranchRollbackResponse response) throws TransactionException;

    @Override
    public AbstractResultMessage onRequest(AbstractMessage request, RpcContext context) {
        if (!(request instanceof AbstractTransactionRequestToRM)) {
            throw new IllegalArgumentException();
        }
        AbstractTransactionRequestToRM transactionRequest = (AbstractTransactionRequestToRM) request;
        transactionRequest.setRMInboundMessageHandler(this);

        return transactionRequest.handle(context);
    }

    @Override
    public void onResponse(AbstractResultMessage response, RpcContext context) {
        throw new ShouldNeverHappenException();

    }
}
