package io.spring2go.seata.rm.datasource.undo;

/**
 * Created by william on May, 2020
 */
public interface UndoLogParser {
    String encode(BranchUndoLog branchUndoLog);

    BranchUndoLog decode(String text);
}
