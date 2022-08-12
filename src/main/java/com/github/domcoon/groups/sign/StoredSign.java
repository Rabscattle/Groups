package com.github.domcoon.groups.sign;

// will be used to retrieve faster updates if no changes have occured
public class StoredSign {
  private final String[] content;

  public StoredSign(String[] content) {
    this.content = content;
  }

  public String[] getContent() {
    return content;
  }
}
