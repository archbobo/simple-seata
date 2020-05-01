package io.spring2go.seata.core.rpc;

/**
 * Created by william on May, 2020
 */
public interface ClientMessageListener {
    /**
     * On message.
     *
     * @param msgId         the msg id
     * @param serverAddress the server address
     * @param msg           the msg
     * @param sender        the sender
     */
    void onMessage(long msgId, String serverAddress, Object msg, ClientMessageSender sender);
}
