package com.github.dschreid.groups.util;

import com.github.dschreid.groups.GroupsPlugin;
import com.github.dschreid.groups.PrefixedException;
import com.github.dschreid.groups.PrefixedExceptionBuilder;
import com.github.dschreid.groups.events.ChangeType;
import com.github.dschreid.groups.events.PermissionChangeEvent;
import com.github.dschreid.groups.lang.LangKeys;
import com.github.dschreid.groups.model.PermissionHolder;
import com.github.dschreid.groups.model.node.Node;
import org.bukkit.Bukkit;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

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
        callEvent(holder, node, ChangeType.SET);
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
        Bukkit.getScheduler().runTask(GroupsPlugin.getInstance(), () -> {
            PermissionChangeEvent permissionChangeEvent =
                    new PermissionChangeEvent(holder, node, ChangeType.REMOVE);
            Bukkit.getPluginManager().callEvent(permissionChangeEvent);
        });
    }
}
