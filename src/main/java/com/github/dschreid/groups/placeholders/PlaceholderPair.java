package com.github.dschreid.groups.placeholders;

import com.github.dschreid.groups.util.Pair;

public class PlaceholderPair extends Pair<String, String> {
    public PlaceholderPair(String s, String s2) {
        super(s, s2);
    }

    public static PlaceholderPair of(String o1, String o2) {
        return new PlaceholderPair(o1, o2);
    }
}
