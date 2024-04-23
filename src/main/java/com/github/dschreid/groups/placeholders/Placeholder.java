package com.github.dschreid.groups.placeholders;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface Placeholder {
    String getReplacement(Player player);
}
