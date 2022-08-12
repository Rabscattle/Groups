package com.github.domcoon.groups.model;

import com.github.domcoon.groups.model.node.Node;
import com.github.domcoon.groups.model.node.StorageNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PermissionCache {
  private final Set<Node> permissionNodes = new HashSet<>();
  private final Set<Node> addedNodes = new HashSet<>();
  private final Set<Node> removedNodes = new HashSet<>();
  private final Set<Node> changedNodes = new HashSet<>();

  public Collection<Node> getStartingWith(String startingWith) {
    Collection<Node> result = new ArrayList<>();
    for (Node permissionNode : this.permissionNodes) {
      if (permissionNode.isExpired()) {
        continue;
      }
      if (permissionNode.getPermission().startsWith(startingWith)) {
        result.add(permissionNode);
      }
    }
    return result;
  }

  public Collection<Node> getMatching(String regex) {
    Collection<Node> result = new ArrayList<>();
    for (Node permissionNode : this.permissionNodes) {
      if (permissionNode.isExpired()) {
        continue;
      }
      if (permissionNode.getPermission().matches(regex)) {
        result.add(permissionNode);
      }
    }
    return result;
  }

  public void loadFromStorage(Collection<Node> permissionNodes) {
    this.permissionNodes.addAll(permissionNodes);
  }

  public Collection<Node> getAll() {
    return new HashSet<>(permissionNodes);
  }

  public int size() {
    return this.permissionNodes.size();
  }

  public Node getExact(String permission) {
    for (Node permissionNode : this.permissionNodes) {
      if (permissionNode.isExpired()) {
        continue;
      }
      if (permissionNode.getPermission().equals(permission)) {
        return permissionNode;
      }
    }
    return null;
  }

  public void addNode(Node node) {
    this.permissionNodes.add(node);
    this.addedNodes.add(node);
  }

  public void removeNode(Node node) {
    this.permissionNodes.remove(node);
    if (node instanceof StorageNode) {
      this.removedNodes.add(node);
    }
  }

  public void addChangedNode(Node exact) {
    if (addedNodes.contains(exact)) {
      return;
    }
    this.changedNodes.add(exact);
  }

  /** Dangerous! Is currently only used to reload a user from storage. */
  public void invalidate() {
    this.permissionNodes.clear();
    this.changedNodes.clear();
    this.removedNodes.clear();
    this.addedNodes.clear();
  }

  public Set<Node> getAddedNodes() {
    return addedNodes;
  }

  public Set<Node> getRemovedNodes() {
    return removedNodes;
  }

  public Set<Node> getChangedNodes() {
    return changedNodes;
  }

  public Set<Node> internal() {
    return this.permissionNodes;
  }
}
