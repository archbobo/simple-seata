package io.spring2go.seata.core.service;

/**
 * Created by william on May, 2020
 */
public interface AddressWatcher {
    void onChange(String[] serverAddressArray);
}
