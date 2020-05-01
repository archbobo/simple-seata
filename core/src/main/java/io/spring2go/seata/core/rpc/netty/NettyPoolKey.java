package io.spring2go.seata.core.rpc.netty;

import com.alibaba.fastjson.JSON;
import io.spring2go.seata.core.protocol.AbstractMessage;

/**
 * Created by william on May, 2020
 */
public class NettyPoolKey {
    private TransactionRole transactionRole;
    private String address;
    private AbstractMessage message;

    /**
     * Instantiates a new Netty pool key.
     *
     * @param transactionRole the client role
     * @param address    the address
     */
    public NettyPoolKey(TransactionRole transactionRole, String address) {
        this.transactionRole = transactionRole;
        this.address = address;
    }

    /**
     * Instantiates a new Netty pool key.
     *
     * @param transactionRole the client role
     * @param address    the address
     * @param message    the message
     */
    public NettyPoolKey(TransactionRole transactionRole, String address, AbstractMessage message) {
        this.transactionRole = transactionRole;
        this.address = address;
        this.message = message;
    }

    /**
     * Gets get client role.
     *
     * @return the get client role
     */
    public TransactionRole getTransactionRole() {
        return transactionRole;
    }

    /**
     * Sets set client role.
     *
     * @param transactionRole the client role
     * @return the client role
     */
    public NettyPoolKey setTransactionRole(TransactionRole transactionRole) {
        this.transactionRole = transactionRole;
        return this;
    }

    /**
     * Gets get address.
     *
     * @return the get address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets set address.
     *
     * @param address the address
     * @return the address
     */
    public NettyPoolKey setAddress(String address) {
        this.address = address;
        return this;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public AbstractMessage getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(AbstractMessage message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    /**
     * The enum Client role.
     */
    public enum TransactionRole {

        /**
         * tm
         */
        TMROLE(1),
        /**
         * rm
         */
        RMROLE(2),
        /**
         * server
         */
        SERVERROLE(3);

        private TransactionRole(int value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return value
         */
        public int getValue() {
            return value;
        }

        /**
         * 状态值
         */
        private int value;
    }
}
