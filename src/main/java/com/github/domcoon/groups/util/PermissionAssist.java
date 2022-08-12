package com.github.domcoon.groups.util;

import com.github.domcoon.groups.PrefixedException;
import com.github.domcoon.groups.PrefixedExceptionBuilder;
import com.github.domcoon.groups.events.ChangeType;
import com.github.domcoon.groups.events.PermissionChangeEvent;
import com.github.domcoon.groups.lang.LangKeys;
import com.github.domcoon.groups.model.PermissionHolder;
import com.github.domcoon.groups.model.node.Node;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;

public class PermissionAssist {
  public static CompletableFuture<Void> setPermission(PermissionHolder holder, Node node)
      throws PrefixedException {
    Node existingNode = holder.getPermissionCache().getExact(node.getPermission());

    if (Objects.equals(existingNode, node)) {
      throw new PrefixedExceptionBuilder()
          .setMessage(LangKeys.NODE_ALREADY_SET)
          .createPrefixedException();
    }

    if (existingNode == null) {
      holder.getPermissionCache().addNode(node);
    } else {
      existingNode.setValue(node.getValue());
      existingNode.setExpiringDate(node.getExpiringDate());
      holder.getPermissionCache().addChangedNode(existingNode);
    }

    holder.invalidate();
    callEvent(holder, null, ChangeType.SET);
    return CompletableFuture.completedFuture(null);
  }

  public static CompletableFuture<Void> removePermission(PermissionHolder holder, String permission)
      throws PrefixedException {
    Node exact = holder.getPermissionCache().getExact(permission);
    if (exact == null) {
      throw new PrefixedExceptionBuilder()
          .setMessage(LangKeys.NODE_NOT_SET)
          .createPrefixedException();
    }

    holder.getPermissionCache().removeNode(exact);
    holder.invalidate();
    callEvent(holder, null, ChangeType.REMOVE);
    return CompletableFuture.completedFuture(null);
  }

  private static void callEvent(PermissionHolder holder, Node node, ChangeType type) {
    PermissionChangeEvent permissionChangeEvent =
        new PermissionChangeEvent(holder, null, ChangeType.REMOVE);
    Bukkit.getPluginManager().callEvent(permissionChangeEvent);
  }
}
