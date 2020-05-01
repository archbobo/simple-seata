package io.spring2go.seata.core.protocol;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by william on May, 2020
 */
public class RpcMessage {
    private static AtomicLong NEXT_ID = new AtomicLong(0);
    public static  long getNextMessageId() {
        return NEXT_ID.incrementAndGet();
    }
    private long id;
    private boolean isAsync;
    private boolean isRequest;
    private boolean isHeartbeat;
    private Object body;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAsync() {
        return isAsync;
    }

    public void setAsync(boolean async) {
        isAsync = async;
    }

    public boolean isRequest() {
        return isRequest;
    }

    public void setRequest(boolean request) {
        isRequest = request;
    }

    public boolean isHeartbeat() {
        return isHeartbeat;
    }

    public void setHeartbeat(boolean heartbeat) {
        isHeartbeat = heartbeat;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
