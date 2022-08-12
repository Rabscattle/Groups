package com.github.domcoon.groups.model.node;

public class StorageNode extends PermissionNode {
  private long id;

  public StorageNode(String permission, long id) {
    super(permission);
    this.id = id;
  }

  public StorageNode(String permission, boolean value, long id) {
    super(permission, value);
    this.id = id;
  }

  public StorageNode(String permission, boolean value, long expiring, long id) {
    super(permission, value, expiring);
    this.id = id;
  }

  public StorageNode(long id, Node node) {
    super(node.getPermission(), node.getValue(), node.getExpiringDate());
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
}
