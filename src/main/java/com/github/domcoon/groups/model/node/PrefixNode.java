package com.github.domcoon.groups.model.node;

public class PrefixNode implements Comparable<PrefixNode> {
  public static String PREFIX_REGEX = "prefix.(\\d+).([a-zA-Z& ]+)";

  private final int weight;
  private final String prefix;

  public PrefixNode(int weight, String prefix) {
    this.weight = weight;
    this.prefix = prefix;
  }

  public PrefixNode(Node node) {
    this.weight = extractWeight(node);
    this.prefix = extractPrefix(node);
  }

  public int getWeight() {
    return weight;
  }

  public String getPrefix() {
    return prefix;
  }

  @Override
  public int compareTo(PrefixNode o) {
    return this.weight - o.weight;
  }

  private int extractWeight(Node o2) {
    String permission = o2.getPermission();
    return Integer.parseInt(permission.split("\\.")[1]);
  }

  private String extractPrefix(Node node) {
    return node.getPermission().split("\\.")[2];
  }

  public Node toPermissionNode() {
    return new PermissionNode("prefix.%d.%s".formatted(weight, prefix));
  }
}
