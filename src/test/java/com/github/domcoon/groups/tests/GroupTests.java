package com.github.domcoon.groups.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.github.domcoon.groups.model.group.Group;
import com.github.domcoon.groups.model.node.PrefixNode;
import com.github.domcoon.groups.model.node.WeightNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GroupTests {
  private final Group group = new Group("admin");

  @Test
  @DisplayName("Group Prefix Test")
  public void testPrefix() {
    String prefix = "&c&l Prefix";
    String prefix2 = "&c&l Higher Prefix";
    PrefixNode prefixNode = new PrefixNode(0, prefix);
    PrefixNode prefixNode2 = new PrefixNode(10, prefix2);
    PrefixNode prefixNode3 = new PrefixNode(20, "Hello");

    group.getPermissionCache().addNode(prefixNode.toPermissionNode());
    group.getPermissionCache().addNode(prefixNode2.toPermissionNode());

    assertEquals(prefix2, group.getPrefix());

    group.getPermissionCache().addNode(prefixNode3.toPermissionNode());
    group.invalidate();

    assertNotEquals(prefix2, group.getPrefix());
  }

  @Test
  @DisplayName("Group Weight Test")
  public void testWeight() {
    WeightNode weightNode10 = new WeightNode(10);

    assertEquals(0, group.getWeight());
    group.invalidate();

    group.getPermissionCache().addNode(weightNode10.toPermissionNode());
    assertEquals(10, group.getWeight());
  }
}
