package io.spring2go.seata.core.model;

/**
 * Created by william on May, 2020
 *
 * Resource that can be managed by Resource Manager and involved into global transaction.
 */
public interface Resource {
    /**
     * Get the resource group id.
     * e.g. master and slave data-source should be with the same resource group id.
     *
     * @return resource group id.
     */
    String getResourceGroupId();


    /**
     * Get the resource id.
     * e.g. url of a data-source could be the id of the db data-source resource.
     *
     * @return resource id.
     */
    String getResourceId();
}
