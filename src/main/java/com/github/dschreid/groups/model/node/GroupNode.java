package com.github.dschreid.groups.model.node;

import java.util.Objects;

public class GroupNode {
    public static String REGEX = "group.([a-zA-Z]+)";

    private final String group;
    private long expiringDate;

    public GroupNode(String group) {
        this.group = group;
        this.expiringDate = 0;
    }

    public GroupNode(String group, long expiringDate) {
        this.group = group;
        this.expiringDate = expiringDate;
    }

    public GroupNode(Node node) {
        this.group = node.getPermission().split("\\.")[1];
        this.expiringDate = node.getExpiringDate();
    }

    public String getGroup() {
        return group;
    }

    public long getExpiringDate() {
        return expiringDate;
    }

    public long getTimeLeft() {
        if (expiringDate == 0) {
            return 0;
        }
        return expiringDate - System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupNode groupNode = (GroupNode) o;
        return expiringDate == groupNode.expiringDate && Objects.equals(group, groupNode.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, expiringDate);
    }

    public Node toPermissionNode() {
        return new NodeBuilder("group.%s".formatted(group), true).expire(expiringDate).build();
    }

    public boolean isExpired() {
        return getTimeLeft() < 0;
    }

    public void withDuration(long duration) {
        this.expiringDate = System.currentTimeMillis() + duration;
    }
}
