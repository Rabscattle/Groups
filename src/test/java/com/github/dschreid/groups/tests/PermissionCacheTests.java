package com.github.dschreid.groups.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.dschreid.groups.model.PermissionCache;
import com.github.dschreid.groups.model.PermissionHolder;
import com.github.dschreid.groups.model.node.Node;
import com.github.dschreid.groups.model.node.NodeBuilder;
import com.github.dschreid.groups.model.user.User;
import java.util.Collection;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PermissionCacheTests {
  private final PermissionHolder permissionHolder = new User(UUID.randomUUID(), "test");

  @Test
  @DisplayName("Adding Permissions")
  public void testAdd() {
    PermissionCache permissionCache = permissionHolder.getPermissionCache();
    permissionCache.addNode(new NodeBuilder("minecraft.*").build());

    assertTrue(() -> permissionHolder.hasPermission("minecraft.*"));
    assertTrue(() -> permissionHolder.hasPermission("minecraft.tree"));
    assertTrue(() -> permissionHolder.hasPermission("minecraft.tree.select"));
    assertEquals(1, permissionCache.getAll().size());
    assertEquals(1, permissionCache.getStartingWith("minecraft.").size());
    assertEquals(1, permissionCache.getMatching("minecraft.([a-zA-Z*]+)").size());
  }

  @Test
  @DisplayName("Removing Permissions")
  public void testRemove() {
    PermissionCache permissionCache = permissionHolder.getPermissionCache();
    permissionCache.addNode(new NodeBuilder("minecraft.*").build());
    permissionCache.addNode(new NodeBuilder("minecraft.free").build());
    permissionCache.addNode(new NodeBuilder("minecraft.vbucks").build());

    Collection<Node> matching = permissionCache.getMatching("minecraft.([a-zA-Z*]+)");
    matching.forEach(permissionCache::removeNode);

    assertEquals(0, permissionCache.size());
  }
}
