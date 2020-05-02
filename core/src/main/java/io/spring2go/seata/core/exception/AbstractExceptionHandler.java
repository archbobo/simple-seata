package io.spring2go.seata.core.exception;

import io.spring2go.seata.core.protocol.ResultCode;
import io.spring2go.seata.core.protocol.transaction.AbstractTransactionRequest;
import io.spring2go.seata.core.protocol.transaction.AbstractTransactionResponse;

/**
 * Created by william on May, 2020
 */
public class AbstractExceptionHandler {
    public interface Callback<T extends AbstractTransactionRequest, S extends AbstractTransactionResponse> {
        void execute(T request, S response) throws TransactionException;
    }

    public void exceptionHandleTemplate(Callback callback, AbstractTransactionRequest request, AbstractTransactionResponse response) {
        try {
            callback.execute(request, response);
            response.setResultCode(ResultCode.Success);

        } catch (TransactionException tex) {
            response.setTransactionExceptionCode(tex.getCode());
            response.setResultCode(ResultCode.Failed);
            response.setMsg("TransactionException[" + tex.getMessage() + "]");

        } catch (RuntimeException rex) {
            response.setResultCode(ResultCode.Failed);
            response.setMsg("RuntimeException[" + rex.getMessage() + "]");
        }
    }
}
