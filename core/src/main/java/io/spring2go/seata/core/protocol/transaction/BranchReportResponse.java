package io.spring2go.seata.core.protocol.transaction;

/**
 * Created by william on May, 2020
 */
public class BranchReportResponse extends AbstractTransactionResponse {
    @Override
    public short getTypeCode() {
        return TYPE_BRANCH_STATUS_REPORT_RESULT;
    }
}
