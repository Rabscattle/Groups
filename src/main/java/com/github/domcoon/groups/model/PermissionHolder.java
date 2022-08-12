package com.github.domcoon.groups.model;

public abstract class PermissionHolder {
  private final PermissionCache permissionCache = new PermissionCache();

  public PermissionCache getPermissionCache() {
    return permissionCache;
  }

  public abstract void invalidate();

  public abstract HolderType getType();

  public abstract Object getUniqueId();
}
