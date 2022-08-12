package com.github.domcoon.groups.util;

import com.github.domcoon.groups.model.PermissionCache;
import java.util.ArrayList;
import java.util.Collection;

public class WildcardResolver {
  private static final String NEW_REGEX = "([a-zA-Z0-9&. ]+)";
  private static final String SEPERATOR = ".";
  private static final String WILDCARD = "*";

  private final PermissionCache cache;

  public WildcardResolver(PermissionCache cache) {
    this.cache = cache;
  }

  public boolean hasPermission(String permission) {
    String current = permission;
    Collection<String> wildcards = new ArrayList<>();
    wildcards.add(WILDCARD);

    while (current != null) {
      if (current.endsWith(SEPERATOR + WILDCARD)) {
        current = current.substring(0, current.length() - (SEPERATOR + WILDCARD).length());
      }
      int index = current.lastIndexOf(SEPERATOR);
      if (index == -1) {
        break;
      }
      current = current.substring(0, index) + SEPERATOR + WILDCARD;
      wildcards.add(current);
    }

    return wildcards.stream()
        .anyMatch(s -> cache.getExact(s) != null && cache.getExact(s).getValue());
  }
}
