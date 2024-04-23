package com.github.dschreid.groups.model.node;

public interface Node {
    String getPermission();

    boolean getValue();

    void setValue(boolean value);

    long getExpiringDate();

    void setExpiringDate(long expiringDate);

    boolean isExpired();

    long getTimeLeft();
}
