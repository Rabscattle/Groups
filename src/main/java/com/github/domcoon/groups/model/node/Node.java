package com.github.domcoon.groups.model.node;

public class Node {
    private final String key;
    private final boolean value;
    private final long expiring;

    public Node(String key, boolean value, long expiring) {
        this.key = key;
        this.value = value;
        this.expiring = expiring;
    }

    public String getKey() {
        return key;
    }

    public boolean isValue() {
        return value;
    }

    public long getExpiring() {
        return expiring;
    }
}
