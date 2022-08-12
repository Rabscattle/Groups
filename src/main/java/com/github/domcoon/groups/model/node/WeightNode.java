package com.github.domcoon.groups.model.node;

public class WeightNode implements Comparable<WeightNode> {
  public static String REGEX = "weight.(\\d+)";

  private final int weight;

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
}
