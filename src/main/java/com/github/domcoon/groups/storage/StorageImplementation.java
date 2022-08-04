package com.github.domcoon.groups.storage;

import com.github.domcoon.groups.GroupsPlugin;
import com.github.domcoon.groups.model.group.Group;

import java.util.Collection;

public interface StorageImplementation {
    void init() throws Exception;
    void close();
    void preInit();

    Group createAndLoadGroup(String group) throws Exception;
    Group loadGroup(String group) throws Exception;

    Collection<Group> getAllGroups() throws Exception;
}
