package io.spring2go.seata.core.context;

import io.spring2go.seata.common.loader.EnhancedServiceLoader;

/**
 * Created by william on May, 2020
 */
public class ContextCoreLoader {
    private static class ContextCoreHolder {
        private static ContextCore INSTANCE;

        static {
            ContextCore contextCore = EnhancedServiceLoader.load(ContextCore.class);
            if (contextCore == null) {
                // Default
                contextCore = new ThreadLocalContextCore();
            }
            INSTANCE = contextCore;
        }
    }

    public static ContextCore load() {
        return ContextCoreHolder.INSTANCE;
    }
}
