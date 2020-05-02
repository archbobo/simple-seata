package io.spring2go.seata.rm.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import io.spring2go.seata.core.model.Resource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by william on May, 2020
 */
public class DataSourceProxy extends AbstractDataSourceProxy implements Resource {
    private String resourceGroupId = "DEFAULT";

    private boolean managed = false;

    public DataSourceProxy(DruidDataSource targetDataSource) {
        super(targetDataSource);
    }
    public DataSourceProxy(DruidDataSource targetDataSource, String resourceGroupId) {
        super(targetDataSource);
        this.resourceGroupId = resourceGroupId;
    }

    private void assertManaged() {
        if (!managed) {
            DataSourceManager.get().registerResource(this);
            managed = true;
        }
    }

    public Connection getPlainConnection() throws SQLException {
        return targetDataSource.getConnection();
    }

    public String getDbType() {
        return targetDataSource.getDbType();
    }

    @Override
    public ConnectionProxy getConnection() throws SQLException {
        assertManaged();
        Connection targetConnection = targetDataSource.getConnection();
        return new ConnectionProxy(this, targetConnection, targetDataSource.getDbType());
    }

    @Override
    public ConnectionProxy getConnection(String username, String password) throws SQLException {
        assertManaged();
        Connection targetConnection = targetDataSource.getConnection(username, password);
        return new ConnectionProxy(this, targetConnection, targetDataSource.getDbType());
    }

    @Override
    public String getResourceGroupId() {
        return resourceGroupId;
    }

    @Override
    public String getResourceId() {
        return targetDataSource.getUrl();
    }
}
