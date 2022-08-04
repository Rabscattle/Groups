package com.github.domcoon.groups.model.user;


import com.github.domcoon.groups.model.group.Group;
import com.github.domcoon.groups.model.group.GroupManager;

import java.util.UUID;

public class User {
    private final UUID uniqueId;

    private final String userName;

    private final String primaryGroup;

    public User(UUID uniqueId, String userName) {
        this.uniqueId = uniqueId;
        this.userName = userName;
        this.primaryGroup = GroupManager.DEFAULT_GROUP;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPrimaryGroup() {
        return primaryGroup;
    }
}
