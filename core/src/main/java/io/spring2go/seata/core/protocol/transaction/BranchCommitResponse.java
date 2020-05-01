package io.spring2go.seata.core.protocol.transaction;

import io.spring2go.seata.core.protocol.AbstractMessage;

/**
 * Created by william on May, 2020
 */
public class BranchCommitResponse extends AbstractBranchEndResponse {
    @Override
    public short getTypeCode() {
        return AbstractMessage.TYPE_BRANCH_COMMIT_RESULT;
    }
}
