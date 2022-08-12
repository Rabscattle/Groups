package com.github.domcoon.groups.storage;

import com.github.domcoon.groups.model.HolderType;
import com.github.domcoon.groups.model.group.Group;
import com.github.domcoon.groups.model.user.User;
import java.util.UUID;

public interface StorageImplementation {
  void init() throws Exception;

  void close();

  void preInit();

  Group createAndLoadGroup(String group) throws Exception;

  Group loadGroup(String group) throws Exception;

  void loadAllGroups() throws Exception;

  void saveGroup(Group group) throws Exception;

  void deleteGroup(String name) throws Exception;

  User loadUser(UUID uniqueId, String name) throws Exception;

  User loadUser(UUID uuid) throws Exception;

  User loadUser(String name) throws Exception;

  void saveUser(User user) throws Exception;

  void deleteExpiredNodes(HolderType type) throws Exception;

  void deleteNodesFromUsers(String permission) throws Exception;
}
