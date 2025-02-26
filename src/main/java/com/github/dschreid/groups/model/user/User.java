package com.github.dschreid.groups.model.user;

import com.github.dschreid.groups.model.HolderType;
import com.github.dschreid.groups.model.PermissionHolder;
import com.github.dschreid.groups.model.node.GroupNode;

import java.util.Objects;
import java.util.UUID;

public class User extends PermissionHolder {
    private final UUID uniqueId;
    private String username;
    private GroupNode storedGroup;

    public User(UUID uniqueId) {
        Objects.requireNonNull(uniqueId, "uuid");
        this.uniqueId = uniqueId;
    }

    public User(UUID uniqueId, String username) {
        Objects.requireNonNull(uniqueId, "uuid");
        Objects.requireNonNull(username, "username");
        this.uniqueId = uniqueId;
        this.username = username.toLowerCase();
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        Objects.requireNonNull(username, "username");
        this.username = username.toLowerCase();
    }

    /**
     * Gets the stored group node This is likely to not represent the most present group of this user.
     *
     * <p>Use {@link com.github.dschreid.groups.model.group.GroupManager#getGroup(User)} to receive the
     * group of this user
     */
    public GroupNode getStoredGroup() {
        return storedGroup;
    }

    public void setStoredGroup(GroupNode storedGroup) {
        this.storedGroup = storedGroup;
    }

    @Override
    public void invalidate() {
        this.storedGroup = null;
    }

    @Override
    public HolderType getType() {
        return HolderType.USER;
    }
}
