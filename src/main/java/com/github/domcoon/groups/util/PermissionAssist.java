package com.github.domcoon.groups.util;

import com.github.domcoon.groups.PrefixedException;
import com.github.domcoon.groups.lang.LangKeys;
import com.github.domcoon.groups.model.PermissionHolder;
import com.github.domcoon.groups.model.node.Node;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class PermissionAssist {
    public static CompletableFuture<Void> setPermission(PermissionHolder holder, Node node) throws PrefixedException {
        Node existingNode = holder.getPermissionCache().getExact(node.getPermission());

        if (Objects.equals(existingNode, node)) {
            throw new PrefixedException(LangKeys.NODE_ALREADY_SET);
        }

        if (existingNode == null) {
            holder.getPermissionCache().addNode(node);
        } else {
            existingNode.setValue(node.getValue());
            existingNode.setExpiringDate(node.getExpiringDate());
            holder.getPermissionCache().addChangedNode(existingNode);
        }

        holder.invalidate();
        return CompletableFuture.completedFuture(null);
    }

    public static CompletableFuture<Void> removePermission(PermissionHolder holder, String permission) throws PrefixedException {
        Node exact = holder.getPermissionCache().getExact(permission);
        if (exact == null) {
            throw new PrefixedException(LangKeys.NODE_NOT_SET);
        }

        holder.getPermissionCache().removeNode(exact);
        holder.invalidate();
        return CompletableFuture.completedFuture(null);
    }
}
