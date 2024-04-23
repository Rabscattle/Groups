package com.github.dschreid.groups.events;

import com.github.dschreid.groups.model.PermissionHolder;
import com.github.dschreid.groups.model.node.Node;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PermissionChangeEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();

    private final PermissionHolder permissionHolder;
    private final Node node;
    private final ChangeType type;

    public PermissionChangeEvent(PermissionHolder permissionHolder, Node node, ChangeType type) {
        this.permissionHolder = permissionHolder;
        this.node = node;
        this.type = type;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public PermissionHolder getPermissionHolder() {
        return permissionHolder;
    }

    public Node getNode() {
        return node;
    }

    public ChangeType getType() {
        return type;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
