package com.github.domcoon.groups;

import com.github.domcoon.groups.placeholders.PlaceholderPair;

public class PrefixedException extends RuntimeException {

  private final PlaceholderPair[] vals;

  public PrefixedException(String message, PlaceholderPair... placeholder) {
    super(message);
    this.vals = placeholder;
  }

  public PlaceholderPair[] getValues() {
    return vals;
  }
}
