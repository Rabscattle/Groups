package com.github.domcoon.groups.model;

import java.util.concurrent.CompletableFuture;

public interface PermissionManager {

  CompletableFuture<Void> setPermission(
      String subject, String permission, boolean value, long expiring);

  CompletableFuture<Void> removePermission(String subject, String permission);
}
