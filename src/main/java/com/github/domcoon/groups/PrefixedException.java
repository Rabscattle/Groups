package com.github.domcoon.groups;

import com.github.domcoon.groups.util.Pair;
import org.bukkit.ChatColor;

public class PrefixedException extends RuntimeException {

    private final Pair<String, String>[] vals;

    @SafeVarargs
    public PrefixedException(String message, Pair<String, String>... placeholder) {
        super(ChatColor.RED + "(!) " + message);
        this.vals = placeholder;
    }

    public Pair<String, String>[] getValues() {
        return vals;
    }
}
