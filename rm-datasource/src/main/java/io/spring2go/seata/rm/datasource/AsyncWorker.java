package io.spring2go.seata.rm.datasource;

import io.spring2go.seata.common.exception.NotSupportYetException;
import io.spring2go.seata.common.thread.NamedThreadFactory;
import io.spring2go.seata.config.ConfigurationFactory;
import io.spring2go.seata.core.exception.TransactionException;
import io.spring2go.seata.core.model.BranchStatus;
import io.spring2go.seata.core.model.ResourceManagerInbound;
import io.spring2go.seata.rm.datasource.undo.UndoLogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static io.spring2go.seata.core.service.ConfigurationKeys.CLIENT_ASYNC_COMMIT_BUFFER_LIMIT;

/**
 * Created by william on May, 2020
 */
public class AsyncWorker implements ResourceManagerInbound {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncWorker.class);

    private static class Phase2Context {

        public Phase2Context(String xid, long branchId, String resourceId, String applicationData) {
            this.xid = xid;
            this.branchId = branchId;
            this.resourceId = resourceId;
            this.applicationData = applicationData;
        }

        String xid;
        long branchId;
        String resourceId;
        String applicationData;
    }

    private static final List<Phase2Context> ASYNC_COMMIT_BUFFER = Collections.synchronizedList(new ArrayList<Phase2Context>());

    private static int ASYNC_COMMIT_BUFFER_LIMIT = ConfigurationFactory.getInstance().getInt(
            CLIENT_ASYNC_COMMIT_BUFFER_LIMIT, 10000);

    private static ScheduledExecutorService timerExecutor;

    @Override
    public BranchStatus branchCommit(String xid, long branchId, String resourceId, String applicationData) throws TransactionException {
        if (ASYNC_COMMIT_BUFFER.size() < ASYNC_COMMIT_BUFFER_LIMIT) {
            ASYNC_COMMIT_BUFFER.add(new Phase2Context(xid, branchId, resourceId, applicationData));
        } else {
            LOGGER.warn("Async commit buffer is FULL. Rejected branch [" + branchId + "/" + xid + "] will be handled by housekeeping later.");
        }
        return BranchStatus.PhaseTwo_Committed;
    }

    public synchronized void init() {
        LOGGER.info("Async Commit Buffer Limit: " + ASYNC_COMMIT_BUFFER_LIMIT);
        timerExecutor = new ScheduledThreadPoolExecutor(1,
                new NamedThreadFactory("AsyncWorker", 1, true));
        timerExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {

                    doBranchCommits();


                } catch (Throwable e) {
                    LOGGER.info("Failed at async committing ... " + e.getMessage());

                }
            }
        }, 10, 1000 * 1, TimeUnit.MILLISECONDS);
    }

    private void doBranchCommits() {
        if (ASYNC_COMMIT_BUFFER.size() == 0) {
            return;
        }
        Map<String, List<Phase2Context>> mappedContexts = new HashMap<>();
        Iterator<Phase2Context> iterator = ASYNC_COMMIT_BUFFER.iterator();
        while (iterator.hasNext()) {
            Phase2Context commitContext = iterator.next();
            List<Phase2Context> contextsGroupedByResourceId = mappedContexts.get(commitContext.resourceId);
            if (contextsGroupedByResourceId == null) {
                contextsGroupedByResourceId = new ArrayList<>();
                mappedContexts.put(commitContext.resourceId, contextsGroupedByResourceId);
            }
            contextsGroupedByResourceId.add(commitContext);

            iterator.remove();

        }

        for (String resourceId : mappedContexts.keySet()) {
            Connection conn = null;
            try {
                try {
                    DataSourceProxy dataSourceProxy = DataSourceManager.get().get(resourceId);
                    conn = dataSourceProxy.getPlainConnection();
                } catch (SQLException sqle) {
                    LOGGER.warn("Failed to get connection for async committing on " + resourceId, sqle);
                    continue;
                }

                List<Phase2Context> contextsGroupedByResourceId = mappedContexts.get(resourceId);
                for (Phase2Context commitContext : contextsGroupedByResourceId) {
                    try {
                        UndoLogManager.deleteUndoLog(commitContext.xid, commitContext.branchId, conn);
                    } catch (Exception ex) {
                        LOGGER.warn("Failed to delete undo log [" + commitContext.branchId + "/" + commitContext.xid + "]", ex);
                    }
                }

            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException closeEx) {
                        LOGGER.warn("Failed to close JDBC resource while deleting undo_log ", closeEx);
                    }
                }
            }


        }


    }

    @Override
    public BranchStatus branchRollback(String xid, long branchId, String resourceId, String applicationData) throws TransactionException {
        throw new NotSupportYetException();

    }
}
