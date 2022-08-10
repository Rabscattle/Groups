package com.github.domcoon.groups.model.user;


import com.github.domcoon.groups.model.HolderType;
import com.github.domcoon.groups.model.PermissionHolder;
import com.github.domcoon.groups.model.node.GroupNode;

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

    /**
     * Gets the stored group node
     * This is likely to not represent the most present
     * group of this user.
     *
     * Use {@link com.github.domcoon.groups.model.group.GroupManager#getGroup(User)}
     * to receive the group of this user
     */
    public GroupNode getStoredGroup() {
        return storedGroup;
    }

    public void setStoredGroup(GroupNode storedGroup) {
        this.storedGroup = storedGroup;
    }

    public void setUsername(String username) {
        Objects.requireNonNull(username, "username");
        this.username = username.toLowerCase();
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
