package io.spring2go.seata.rm;

import io.spring2go.seata.core.rpc.netty.RmMessageListener;
import io.spring2go.seata.core.rpc.netty.RmRpcClient;
import io.spring2go.seata.rm.datasource.AsyncWorker;
import io.spring2go.seata.rm.datasource.DataSourceManager;

/**
 * Created by william on May, 2020
 */
public class RMClientAT {
    public static void init(String applicationId, String transactionServiceGroup) {
        RmRpcClient rmRpcClient = RmRpcClient.getInstance(applicationId, transactionServiceGroup);
        AsyncWorker asyncWorker = new AsyncWorker();
        asyncWorker.init();
        DataSourceManager.init(asyncWorker);
        rmRpcClient.setResourceManager(DataSourceManager.get());
        rmRpcClient.setClientMessageListener(new RmMessageListener(new RMHandlerAT()));
        rmRpcClient.init();
    }
}
