package com.github.domcoon.groups.placeholders;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface Placeholder {
    String replace(Player player);
}
