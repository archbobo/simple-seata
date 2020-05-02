package io.spring2go.seata.rm.datasource;

import io.spring2go.seata.common.exception.ShouldNeverHappenException;
import io.spring2go.seata.rm.datasource.undo.SQLUndoLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by william on May, 2020
 */
public class ConnectionContext {
    private String xid;
    private Long branchId;
    private List<String> lockKeysBuffer = new ArrayList<>();
    private List<SQLUndoLog> sqlUndoItemsBuffer = new ArrayList<>();

    void appendLockKey(String lockKey) {
        lockKeysBuffer.add(lockKey);
    }

    void appendUndoItem(SQLUndoLog sqlUndoLog) {
        sqlUndoItemsBuffer.add(sqlUndoLog);
    }

    public boolean inGlobalTransaction() {
        return xid != null;
    }

    public boolean isBranchRegistered() {
        return branchId != null;
    }

    void bind(String xid) {
        if (xid == null) {
            throw new IllegalArgumentException("xid should not be null");
        }
        if (!inGlobalTransaction()) {
            setXid(xid);
        } else {
            if (!this.xid.equals(xid)) {
                throw new ShouldNeverHappenException();
            }
        }
    }

    public boolean hasUndoLog() {
        return sqlUndoItemsBuffer.size() > 0;
    }

    public String getXid() {
        return xid;
    }

    void setXid(String xid) {
        this.xid = xid;
    }

    public Long getBranchId() {
        return branchId;
    }

    void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    void reset() {
        xid = null;
        branchId = null;
        lockKeysBuffer.clear();
        sqlUndoItemsBuffer.clear();
    }

    void reset(String xid) {
        this.xid = xid;
        branchId = null;
        lockKeysBuffer.clear();
        sqlUndoItemsBuffer.clear();
    }

    public String buildLockKeys() {
        if (lockKeysBuffer.isEmpty()) {
            return null;
        }
        StringBuffer appender = new StringBuffer();
        Iterator<String> iterable = lockKeysBuffer.iterator();
        while (iterable.hasNext()) {
            appender.append(iterable.next());
            if (iterable.hasNext()) {
                appender.append(";");
            }
        }
        return appender.toString();
    }

    public List<SQLUndoLog> getUndoItems() {
        return sqlUndoItemsBuffer;
    }
}
