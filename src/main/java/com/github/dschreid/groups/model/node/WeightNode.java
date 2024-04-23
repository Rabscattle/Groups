package com.github.dschreid.groups.model.node;

public class WeightNode implements Comparable<WeightNode> {
    public static String REGEX = "weight.(\\d+)";

    private final int weight;

    public WeightNode(int weight) {
        this.weight = weight;
    }

    public WeightNode(Node group) {
        this.weight = Integer.parseInt(group.getPermission().split("\\.")[1]);
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public int compareTo(WeightNode o) {
        return this.weight - o.weight;
    }

    public Node toPermissionNode() {
        return new NodeBuilder("weight.%d".formatted(this.weight)).build();
    }
}
