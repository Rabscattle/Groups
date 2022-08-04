package com.github.domcoon.groups.model.group;

public class Group {
    private String name;
    private int weight;

    public Group(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

}