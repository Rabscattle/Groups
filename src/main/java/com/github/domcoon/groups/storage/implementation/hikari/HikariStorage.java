package com.github.domcoon.groups.storage.implementation.hikari;

import com.github.domcoon.groups.GroupsPlugin;
import com.github.domcoon.groups.model.HolderType;
import com.github.domcoon.groups.model.PermissionCache;
import com.github.domcoon.groups.model.PermissionHolder;
import com.github.domcoon.groups.model.group.Group;
import com.github.domcoon.groups.model.node.Node;
import com.github.domcoon.groups.model.node.StorageNode;
import com.github.domcoon.groups.model.user.User;
import com.github.domcoon.groups.storage.StorageImplementation;
import com.github.domcoon.groups.util.Pair;
import com.github.domcoon.groups.util.SQLScriptReader;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class HikariStorage implements StorageImplementation {
    /**
     * Determines if a player is in the users table
     */
    private static String USER_LOADED_META = "loaded";

    private static final String INSERT_GROUP_QUERY = "INSERT INTO 'groups' (name) VALUES(?)";
    private static final String SELECT_GROUPS_QUERY = "SELECT * FROM 'groups'";
    private static final String DELETE_GROUP_QUERY = "DELETE FROM 'groups' WHERE 'name'=?";
    private static final String REMOVE_GROUP_NODES_QUERY = "DELETE FROM 'group_permissions' WHERE 'group'=?";

    private static final String SELECT_USER_QUERY = "SELECT * FROM 'users' WHERE 'uuid'=?";
    private static final String INSERT_USER_QUERY = "INSERT INTO 'users' (uuid, username) VALUES(?,?)";
    private static final String SELECT_USER_BY_NAME = "SELECT uuid FROM users WHERE 'name'=?";
    private static final String SELECT_USERS_QUERY = "SELECT * FROM 'users'";
    private static final String DELETE_USER_NODES_QUERY = "DELETE FROM 'user_permissions' WHERE 'permission'=?";

    private static final String LOAD_ALL_NODES_QUERY = "SELECT * FROM '{table}' WHERE '{id_row}'=?";
    private static final String UPDATE_NODE_QUERY = "UPDATE '{table}' SET 'value'=?, 'expiring'=? WHERE id=?";
    private static final String INSERT_NODE_QUERY = "INSERT INTO '{table}' ('{id_row}', permission, value, expiring) VALUES(?,?,?,?)";
    private static final String DELETE_NODE_QUERY = "DELETE FROM '{table}' WHERE 'id'=?";
    private static final String DELETE_EXPIRED_NODES_QUERY = "DELETE FROM '{table}' WHERE 'expiring'!=0 AND 'expiring'<?";

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
    public Group createAndLoadGroup(String name) throws Exception {
        // Create Group First
        try (final Connection connection = getConnection()) {
            try (final PreparedStatement preparedStatement = connection.prepareStatement(apply(INSERT_GROUP_QUERY));) {
                preparedStatement.setString(1, name);
                preparedStatement.execute();
            }
        }
        return loadGroup(name);
    }

    @Override
    public Group loadGroup(String name) throws Exception {
        try (final Connection connection = getConnection()) {
            Collection<String> groups = selectAllGroups(connection);
            if (!groups.contains(name))
                return null;

            Group group = this.plugin.getGroupManager().getOrCreate(name);

            Collection<Node> storageNodes = selectAllNodes(connection, group);
            group.getPermissionCache().loadFromStorage(storageNodes);
            return group;
        }
    }

    @Override
    public void loadAllGroups() throws Exception {
        try (Connection connection = getConnection()) {
            Collection<String> groups = selectAllGroups(connection);

            for (String group : groups) {
                Group memoryGroup = this.plugin.getGroupManager().getOrCreate(group);

                Collection<Node> storageNodes = selectAllNodes(connection, memoryGroup);
                memoryGroup.getPermissionCache().loadFromStorage(storageNodes);
            }
        }
    }

    private Collection<String> selectAllGroups(Connection connection) throws Exception {
        List<String> result = new ArrayList<>();
        try (final PreparedStatement preparedStatement = connection.prepareStatement(apply(SELECT_GROUPS_QUERY));) {
            try (final ResultSet resultSet = preparedStatement.executeQuery();) {
                while (resultSet.next()) {
                    final String groupName = resultSet.getString("name");
                    result.add(groupName);
                }
            }
        }
        return result;
    }

    @Override
    public void saveGroup(Group group) throws Exception {
        this.savePermissionHolder(group);
    }

    @Override
    public void deleteGroup(String name) throws Exception {
        try (Connection connection = getConnection()) {
            try (PreparedStatement st = connection.prepareStatement(apply(DELETE_GROUP_QUERY))) {
                st.setString(1, name);
                st.execute();
            }

            try (PreparedStatement st = connection.prepareStatement(apply(REMOVE_GROUP_NODES_QUERY))) {
                st.setString(1, name);
                st.execute();
            }
        }
    }

    private void savePermissionHolder(PermissionHolder holder) throws SQLException {
        PermissionCache permissionCache = holder.getPermissionCache();

        try (Connection connection = getConnection()) {
            for (Node addedNode : permissionCache.getAddedNodes()) {
                this.addNode(connection, holder, addedNode);
            }

            for (Node removedNode : permissionCache.getRemovedNodes()) {
                if (removedNode instanceof StorageNode)
                    this.removeNode(connection, holder, (StorageNode) removedNode);
            }

            for (Node updatedNode : permissionCache.getChangedNodes()) {
                if (updatedNode instanceof StorageNode)
                    this.updateNode(connection, holder, (StorageNode) updatedNode);
            }

            permissionCache.getAddedNodes().clear();
            permissionCache.getRemovedNodes().clear();
            permissionCache.getChangedNodes().clear();
        }
    }

    private void addNode(Connection connection, PermissionHolder holder, Node node) throws SQLException {
        TableIdRowPair pair = getRowPair(holder.getType());

        String query = apply(INSERT_NODE_QUERY)
                .replace("{table}", pair.getTable())
                .replace("{id_row}", pair.getRowId());

        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, holder.getUniqueId().toString());
            st.setString(2, node.getPermission());
            st.setBoolean(3, node.getValue());
            st.setLong(4, node.getExpiringDate());

            try (ResultSet resultSet = st.getGeneratedKeys()) {
                int id = resultSet.getInt("id");
                this.replaceNode(holder, node, new StorageNode(id, node));
            }
        }
    }

    private void replaceNode(PermissionHolder holder, Node old, Node newNode) {
        holder.getPermissionCache().internal().remove(old);
        holder.getPermissionCache().internal().add(newNode);
    }

    private void removeNode(Connection connection, PermissionHolder holder, StorageNode node) throws SQLException {
        TableIdRowPair pair = getRowPair(holder.getType());
        String query = apply(DELETE_NODE_QUERY).replace("{table}", pair.getTable());

        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setLong(1, node.getId());

            st.execute();
        }
    }

    private void updateNode(Connection connection, PermissionHolder holder, StorageNode node) throws SQLException {
        TableIdRowPair pair = getRowPair(holder.getType());
        String query = apply(UPDATE_NODE_QUERY).replace("{table}", pair.getTable());

        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setBoolean(1, node.getValue());
            st.setLong(2, node.getExpiringDate());
            st.setLong(3, node.getId());

            st.execute();
        }
    }

    @Override
    public User loadUser(UUID uniqueId, String name) throws Exception {
        User user = this.plugin.getUserManager().getOrCreate(uniqueId);
        user.setUsername(name);

        try (Connection connection = getConnection()) {
            Collection<Node> storageNodes = selectAllNodes(connection, user);
            user.getPermissionCache().loadFromStorage(storageNodes);
        }

        return user;
    }

    @Override
    public User loadUser(UUID uuid) throws Exception {
        try (Connection connection = getConnection()) {
            Map<UUID, String> userTable = selectAllUsers(connection);
            if (!userTable.containsKey(uuid)) {
                return null;
            }

            return loadUser(uuid, userTable.get(uuid));
        }
    }

    @Override
    public User loadUser(String name) throws Exception {
        try (Connection connection = getConnection()) {
            Map<UUID, String> userTable = selectAllUsers(connection);

            UUID found = null;
            for (Map.Entry<UUID, String> entry : userTable.entrySet()) {
                if (Objects.equals(entry.getValue(), name)) {
                    found = entry.getKey();
                }
            }

            if (found == null)
                return null;

            return loadUser(found, name);
        }
    }

    private UUID getUUIDByName(Connection connection, String name) throws SQLException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(apply(SELECT_USER_BY_NAME));) {
            preparedStatement.setString(1, name);
            try (final ResultSet resultSet = preparedStatement.executeQuery();) {
                if (resultSet.next()) {
                    return UUID.fromString(resultSet.getString("uuid"));
                }
                return null;
            }
        }
    }

    @Override
    public void saveUser(User user) throws Exception {
        this.insertUser(user);
        this.savePermissionHolder(user);
    }

    private void insertUser(User user) throws SQLException {
        try (Connection connection = getConnection()) {
            if (containsUser(connection, user.getUniqueId())) {
                return;
            }

            try (PreparedStatement statement = connection.prepareStatement(apply(INSERT_USER_QUERY))) {
                statement.setString(1, user.getUniqueId().toString());
                statement.setString(2, user.getUsername());
                statement.execute();
            }
        }
    }

    private boolean containsUser(Connection connection, UUID user) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(apply(SELECT_USER_QUERY))) {
            statement.setString(1, user.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return true;
            }
        }
        return false;
    }

    private Map<UUID, String> selectAllUsers(Connection connection) throws Exception {
        Map<UUID, String> result = new HashMap<>();
        try (final PreparedStatement preparedStatement = connection.prepareStatement(apply(SELECT_USERS_QUERY));) {
            try (final ResultSet resultSet = preparedStatement.executeQuery();) {
                while (resultSet.next()) {
                    final UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                    final String username = resultSet.getString("username");
                    result.put(uuid, username);
                }
            }
        }
        return result;
    }


    public Collection<Node> selectAllNodes(Connection c, PermissionHolder holder) throws Exception {
        ArrayList<Node> result = new ArrayList<>();
        TableIdRowPair pair = getRowPair(holder.getType());

        String query = apply(LOAD_ALL_NODES_QUERY)
                .replace("{table}", pair.getTable())
                .replace("{id_row}", pair.getRowId());

        try (PreparedStatement statement = c.prepareStatement(query)) {
            statement.setString(1, holder.getUniqueId().toString());

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(readNode(rs));
                }
            }
        }
        return result;
    }

    private TableIdRowPair getRowPair(HolderType type) {
        String table = null;
        String idField = null;
        switch (type) {
            case USER -> {
                table = "user_permissions";
                idField = "uuid";
            }
            case GROUP -> {
                table = "group_permissions";
                idField = "group";
            }
        }
        return new TableIdRowPair(table, idField);
    }

    private Node readNode(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String permission = rs.getString("permission");
        boolean value = rs.getBoolean("value");
        long expiring = rs.getLong("expiring");

        return new StorageNode(permission, value, expiring, id);
    }

    @Override
    public void deleteExpiredNodes(HolderType type) throws Exception {
        TableIdRowPair rowPair = getRowPair(type);
        try (Connection connection = getConnection()) {
            String apply = apply(DELETE_EXPIRED_NODES_QUERY)
                    .replace("{table}", rowPair.getTable());
            try (PreparedStatement statement = connection.prepareStatement(apply)) {
                statement.setLong(1, System.currentTimeMillis());
                statement.execute();
            }
        }
    }

    @Override
    public void deleteNodesFromUsers(String permission) throws Exception {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(apply(DELETE_USER_NODES_QUERY))) {
                statement.setString(1, permission);
                statement.execute();
            }
        }

        for (User user : this.plugin.getUserManager().getAll()) {
            user.invalidate();
            user.getPermissionCache().invalidate();
            this.loadUser(user.getUniqueId(), user.getUsername());
        }
    }

    /**
     * Replaces invalid characters with sql applicable ones
     */
    private String apply(String statement) {
        return statement.replace('\'', '`');
    }

    public static class TableIdRowPair extends Pair<String, String> {
        public TableIdRowPair(String table, String rowId) {
            super(table, rowId);
        }

        public String getTable() {
            return super.getK();
        }

        public String getRowId() {
            return super.getV();
        }
    }
}
