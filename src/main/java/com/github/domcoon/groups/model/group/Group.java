package com.github.domcoon.groups.model.group;

import com.github.domcoon.groups.model.HolderType;
import com.github.domcoon.groups.model.PermissionHolder;
import com.github.domcoon.groups.model.node.Node;
import com.github.domcoon.groups.model.node.PrefixNode;
import com.github.domcoon.groups.model.node.WeightNode;
import java.util.Collection;
import java.util.Objects;

public class Group extends PermissionHolder implements Comparable<Group> {
  private final String name;
  private String storedPrefix;
  private Integer storedWeight;

  public Group(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public void invalidate() {
    this.storedPrefix = null;
    this.storedWeight = null;
  }

  @Override
  public HolderType getType() {
    return HolderType.GROUP;
  }

  @Override
  public String getUniqueId() {
    return name;
  }

  public String getPrefix() {
    if (storedPrefix != null) {
      return storedPrefix;
    }

    Collection<Node> nodes = getPermissionCache().getMatching(PrefixNode.PREFIX_REGEX);
    if (nodes.isEmpty()) {
      return storedPrefix = "";
    }

    PrefixNode bestNode = nodes.stream().map(PrefixNode::new).max(Comparable::compareTo).get();

    return storedPrefix = bestNode.getPrefix();
  }

  public int getWeight() {
    if (storedWeight != null) {
      return storedWeight;
    }

    Collection<Node> nodes = getPermissionCache().getMatching(WeightNode.REGEX);
    if (nodes.isEmpty()) {
      return storedWeight = 0;
    }

    WeightNode bestNode = nodes.stream().map(WeightNode::new).max(Comparable::compareTo).get();

    return storedWeight = bestNode.getWeight();
  }

  public String getStoredPrefix() {
    return storedPrefix;
  }

  public void setStoredPrefix(String storedPrefix) {
    this.storedPrefix = storedPrefix;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Group group = (Group) o;
    return Objects.equals(name, group.name) && Objects.equals(storedPrefix, group.storedPrefix);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, storedPrefix);
  }

  @Override
  public int compareTo(Group o) {
    return this.getWeight() - o.getWeight();
  }
}
