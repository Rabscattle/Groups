package com.github.dschreid.groups.model;

import com.github.dschreid.groups.model.node.Node;
import com.github.dschreid.groups.util.WildcardResolver;

public abstract class PermissionHolder {
    private final PermissionCache permissionCache = new PermissionCache();

    public PermissionCache getPermissionCache() {
        return permissionCache;
    }

    public abstract void invalidate();

    public abstract HolderType getType();

    public abstract Object getUniqueId();

    public final boolean hasPermission(String permission) {
        Node exact = getPermissionCache().getExact(permission);
        if (exact != null) {
            return exact.getValue();
        }
        return new WildcardResolver(permissionCache).hasPermission(permission);
    }
}
