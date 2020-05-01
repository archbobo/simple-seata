package io.spring2go.seata.core.model;

import io.spring2go.seata.core.exception.TransactionException;

/**
 * Created by william on May, 2020
 *
 * Transaction Manager.
 *
 * Define a global transaction and control it.
 */
public interface TransactionManager {
    /**
     * Begin a new global transaction.
     *
     * @param applicationId ID of the application who begins this transaction.
     * @param transactionServiceGroup ID of the transaction service group.
     * @param name Give a name to the global transaction.
     * @param timeout Timeout of the global transaction.
     * @return XID of the global transaction
     * @throws TransactionException Any exception that fails this will be wrapped with TransactionException and thrown out.
     */
    String begin(String applicationId, String transactionServiceGroup, String name, int timeout) throws TransactionException;

    /**
     * Global commit.
     *
     * @param xid XID of the global transaction.
     * @return Status of the global transaction after committing.
     * @throws TransactionException Any exception that fails this will be wrapped with TransactionException and thrown out.
     */
    GlobalStatus commit(String xid) throws TransactionException;

    /**
     * Global rollback.
     *
     * @param xid XID of the global transaction
     * @return Status of the global transaction after rollbacking.
     * @throws TransactionException Any exception that fails this will be wrapped with TransactionException and thrown out.
     */
    GlobalStatus rollback(String xid) throws TransactionException;

    /**
     * Get current status of the given transaction.
     * @param xid XID of the global transaction.
     * @return Current status of the global transaction.
     * @throws TransactionException Any exception that fails this will be wrapped with TransactionException and thrown out.
     */
    GlobalStatus getStatus(String xid) throws TransactionException;

}
