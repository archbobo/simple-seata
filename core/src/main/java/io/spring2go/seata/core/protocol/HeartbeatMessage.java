package io.spring2go.seata.core.protocol;

/**
 * Created by william on May, 2020
 */
public class HeartbeatMessage {
    private static final long serialVersionUID = -985316399527884899L;
    private boolean pingOrPong = true;
    public static HeartbeatMessage PING = new HeartbeatMessage(true);
    public static HeartbeatMessage PONG = new HeartbeatMessage(false);

    private HeartbeatMessage(boolean pingOrPong) {
        this.pingOrPong = pingOrPong;
    }

    @Override
    public String toString() {
        return this.pingOrPong ? "seata ping" : "seata pong";
    }
}
