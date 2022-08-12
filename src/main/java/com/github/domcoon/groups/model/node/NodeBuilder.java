package com.github.domcoon.groups.model.node;

public class NodeBuilder {
  private final String permission;
  private final boolean value;
  private long expiry;

  public NodeBuilder(String permission) {
    this.permission = permission;
    this.value = true;
  }

  public NodeBuilder(String permission, boolean value) {
    this.permission = permission;
    this.value = value;
  }

  public NodeBuilder duration(long duration) {
    if (duration == 0) {
      return this;
    }

    this.expiry = System.currentTimeMillis() + duration;
    return this;
  }

  public NodeBuilder expire(long date) {
    this.expiry = date;
    return this;
  }

  public Node build() {
    return new PermissionNode(permission, value, expiry);
  }
}
