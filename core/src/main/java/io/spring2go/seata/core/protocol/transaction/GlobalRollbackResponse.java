package io.spring2go.seata.core.protocol.transaction;

/**
 * Created by william on May, 2020
 */
public class GlobalRollbackResponse extends AbstractGlobalEndResponse {
    @Override
    public short getTypeCode() {
        return TYPE_GLOBAL_ROLLBACK_RESULT;
    }
}
