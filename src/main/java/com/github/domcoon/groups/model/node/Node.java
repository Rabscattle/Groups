package com.github.domcoon.groups.model.node;

public interface Node {
    String getPermission();

    boolean getValue();

    long getExpiringDate();

    void setExpiringDate(long expiringDate);

    boolean isExpired();

    long getTimeLeft();

    void setValue(boolean value);

}
