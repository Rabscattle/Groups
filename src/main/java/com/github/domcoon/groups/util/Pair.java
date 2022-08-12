package com.github.domcoon.groups.util;

import java.util.Objects;

public class Pair<K, V> {
  private final K k;
  private final V v;

  public Pair(K k, V v) {
    this.k = k;
    this.v = v;
  }

  public K getK() {
    return k;
  }

  public V getV() {
    return v;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Pair)) {
      return false;
    }
    Pair<?, ?> pair = (Pair<?, ?>) o;
    return Objects.equals(k, pair.k) && Objects.equals(v, pair.v);
  }

  @Override
  public int hashCode() {
    return Objects.hash(k, v);
  }
}
