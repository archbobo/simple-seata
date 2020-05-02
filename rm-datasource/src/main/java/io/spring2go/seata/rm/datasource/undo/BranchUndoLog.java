package io.spring2go.seata.rm.datasource.undo;

import java.util.List;

/**
 * Created by william on May, 2020
 */
public class BranchUndoLog {
    private String xid;

    private long branchId;

    private List<SQLUndoLog> sqlUndoLogs;

    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    public long getBranchId() {
        return branchId;
    }

    public void setBranchId(long branchId) {
        this.branchId = branchId;
    }

    public List<SQLUndoLog> getSqlUndoLogs() {
        return sqlUndoLogs;
    }

    public void setSqlUndoLogs(List<SQLUndoLog> sqlUndoLogs) {
        this.sqlUndoLogs = sqlUndoLogs;
    }
}
