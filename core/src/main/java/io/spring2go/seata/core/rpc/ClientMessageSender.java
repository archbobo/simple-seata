package io.spring2go.seata.core.rpc;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by william on May, 2020
 */
public interface ClientMessageSender {
    /**
     * Send msg with response object.
     *
     * @param msg     the msg
     * @param timeout the timeout
     * @return the object
     * @throws IOException the io exception
     * @throws TimeoutException the timeout exception
     */
    Object sendMsgWithResponse(Object msg, long timeout) throws TimeoutException;

    /**
     * Send msg with response object.
     *
     * @param serverAddress the server address
     * @param msg           the msg
     * @param timeout       the timeout
     * @return the object
     * @throws IOException the io exception
     * @throws TimeoutException the timeout exception
     */
    Object sendMsgWithResponse(String serverAddress, Object msg, long timeout) throws TimeoutException;

    /**
     * Send msg with response object.
     *
     * @param msg the msg
     * @return the object
     * @throws IOException the io exception
     * @throws TimeoutException the timeout exception
     */
    Object sendMsgWithResponse(Object msg) throws TimeoutException;

    /**
     * Send response.
     *
     * @param msgId         the msg id
     * @param serverAddress the server address
     * @param msg           the msg
     */
    void sendResponse(long msgId, String serverAddress, Object msg);
}
