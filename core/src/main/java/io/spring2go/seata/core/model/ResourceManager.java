package io.spring2go.seata.core.model;

import java.util.Map;

/**
 * Created by william on May, 2020
 *
 * Resource Manager: common behaviors.
 */
public interface ResourceManager extends ResourceManagerInbound, ResourceManagerOutbound {
    /**
     * Register a Resource to be managed by Resource Manager.
     *
     * @param resource The resource to be managed.
     */
    void registerResource(Resource resource);

    /**
     * Unregister a Resource from the Resource Manager.
     *
     * @param resource The resource to be removed.
     */
    void unregisterResource(Resource resource);

    /**
     * Get all resources managed by this manager.
     *
     * @return resourceId -> Resource Map
     */
    Map<String, Resource> getManagedResources();
}
