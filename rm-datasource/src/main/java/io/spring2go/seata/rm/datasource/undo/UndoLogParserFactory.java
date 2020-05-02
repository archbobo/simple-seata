package io.spring2go.seata.rm.datasource.undo;

/**
 * Created by william on May, 2020
 */
public class UndoLogParserFactory {
    private static class SingletonHolder {
        private static final UndoLogParser INSTANCE = new JSONBasedUndoLogParser();
    }

    public static final UndoLogParser getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
