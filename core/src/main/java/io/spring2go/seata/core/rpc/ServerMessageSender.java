package io.spring2go.seata.core.rpc;

import io.netty.channel.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by william on May, 2020
 */
public interface ServerMessageSender {
    /**
     * Send request to client by the upper level.
     * Actually, just BranchCommitRequest/BranchRollbackRequest will be sent from server to RM client.
     * No case for sending a request to TM client. ResourceId is useless by case of sending a TM request.
     *
     * @param resourceId resourceId
     * @param clientIP IP of the target client
     * @param applicationId applicationId of the target client
     * @param request request to be sent
     */
    void sendRequest(String resourceId, String clientIP, String applicationId, Object request /* TODO: this should be AbstractMessage */);

    /**
     * @param msgId
     * @param dbKey
     * @param clientIp
     * @param clientAppName
     * @param msg
     */
    void sendResponse(long msgId, String dbKey, String clientIp, String clientAppName, Object msg);

    /**
     * @param msgId
     * @param channel
     * @param msg
     */
    void sendResponse(long msgId, Channel channel, Object msg);

    /**
     * 同步调用client
     *
     * @param dbKey
     * @param clientIp
     * @param clientAppName
     * @param msg
     * @return
     * @throws IOException
     */
    Object sendSynRequest(String dbKey, String clientIp, String clientAppName,
                          Object msg, long timeout) throws IOException, TimeoutException;

    /**
     * 同步调用client
     *
     * @param dbKey
     * @param clientIp
     * @param clientAppName
     * @param msg
     * @return
     * @throws IOException
     */
    Object sendSynRequest(String dbKey, String clientIp, String clientAppName,
                          Object msg) throws IOException, TimeoutException;
}
