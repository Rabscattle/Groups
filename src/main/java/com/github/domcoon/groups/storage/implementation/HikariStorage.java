package com.github.domcoon.groups.storage.implementation;

import com.github.domcoon.groups.GroupsPlugin;
import com.github.domcoon.groups.model.group.Group;
import com.github.domcoon.groups.storage.StorageImplementation;
import com.github.domcoon.groups.util.SQLScriptReader;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HikariStorage implements StorageImplementation {
    private static final String INSERT_GROUP_QUERY = "INSERT INTO groups (name) VALUES(?)";
    private static final String LOAD_GROUP_QUERY = "SELECT (name, weight) FROM groups WHERE name=?";
    private static final String LOAD_ALL_GROUPS_QUERY = "SELECT * FROM groups";

    private final HikariStorageBean storageBean = new HikariStorageBean();
    private final GroupsPlugin plugin;
    private HikariDataSource dataSource;

    public HikariStorage(GroupsPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public void preInit() {
        plugin.getPluginConfiguration().addBeans(this.storageBean);
    }

    @Override
    public void init() throws Exception {
        HikariConfig config = new HikariConfig();
        this.storageBean.fillHikariConfig(config);

        this.dataSource = new HikariDataSource(config);

        try (final Connection connection = getConnection()) {
            if (!tableExists(connection)) {
                this.runDefault(connection);
            }
        }
    }

    private boolean tableExists(Connection connection) throws SQLException {
        try (ResultSet rs = connection.getMetaData().getTables(connection.getCatalog(), null, "%", null)) {
            while (rs.next()) {
                if (rs.getString(3).equalsIgnoreCase("users")) {
                    return true;
                }
            }
            return false;
        }
    }

    private void runDefault(Connection connection) throws Exception {
        final InputStream resource = plugin.getResource("db.sql");
        if (resource == null)
            return;
        final SQLScriptReader sqlScriptReader = new SQLScriptReader(resource);

        try (final Statement statement = connection.createStatement();) {
            for (String query : sqlScriptReader.getQueries()) {
                statement.addBatch(query);
            }
            statement.executeBatch();
        }

    }

    public Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        if (connection == null)
            throw new SQLException("Unable to get a connection from the pool. (getConnection returned null)");
        return connection;
    }

    @Override
    public void close() {
        try {
            getConnection().close();
        } catch (Exception ex) {
            // ignored
        }
    }

    @Override
    public Group createAndLoadGroup(String name) throws SQLException {
        try (final Connection connection = getConnection()) {
            try (final PreparedStatement preparedStatement = connection.prepareStatement(INSERT_GROUP_QUERY);) {
                preparedStatement.setString(1, name);
                preparedStatement.execute();
            }
        }
        return loadGroup(name);
    }

    @Override
    public Group loadGroup(String name) throws SQLException {
        try (final Connection connection = getConnection()) {
            try (final PreparedStatement preparedStatement = connection.prepareStatement(LOAD_GROUP_QUERY);) {
                preparedStatement.setString(1, name);
                try (final ResultSet resultSet = preparedStatement.executeQuery();) {
                    if (resultSet.next()) {
                        final String groupName = resultSet.getString("name");
                        final int weight = resultSet.getInt("weight");
                        return new Group(groupName, weight);
                    }
                }
            }
        }

        return null;
    }

    @Override
    public Collection<Group> getAllGroups() throws Exception {
        try (final Connection connection = getConnection()) {
            return selectAllGroups(connection);
        }
    }

    private Collection<Group> selectAllGroups(Connection connection) throws Exception {
        List<Group> result = new ArrayList<>();
        try (final PreparedStatement preparedStatement = connection.prepareStatement(LOAD_ALL_GROUPS_QUERY);) {
            try (final ResultSet resultSet = preparedStatement.executeQuery();) {
                while (resultSet.next()) {
                    final String groupName = resultSet.getString("name");
                    final int weight = resultSet.getInt("weight");
                    result.add(new Group(groupName, weight));
                }
            }
        }
        return result;
    }
}
