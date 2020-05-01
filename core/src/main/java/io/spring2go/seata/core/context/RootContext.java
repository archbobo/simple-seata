package io.spring2go.seata.core.context;

import io.spring2go.seata.common.exception.ShouldNeverHappenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by william on May, 2020
 */
public class RootContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(RootContext.class);

    public static final String KEY_XID = "TX_XID";

    private static ContextCore CONTEXT_HOLDER = ContextCoreLoader.load();

    public static String getXID() {
        return CONTEXT_HOLDER.get(KEY_XID);
    }

    public static void bind(String xid) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("bind " + xid);
        }
        CONTEXT_HOLDER.put(KEY_XID, xid);
    }

    public static String unbind() {
        String xid = CONTEXT_HOLDER.remove(KEY_XID);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("unbind " + xid);
        }
        return xid;
    }

    public static boolean inGlobalTransaction() {
        return CONTEXT_HOLDER.get(KEY_XID) != null;
    }

    public static void assertNotInGlobalTransaction() {
        if (inGlobalTransaction()) {
            throw new ShouldNeverHappenException();
        }
    }
}
