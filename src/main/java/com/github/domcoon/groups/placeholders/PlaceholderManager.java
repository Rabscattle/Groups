package com.github.domcoon.groups.placeholders;

import com.github.domcoon.groups.GroupsPlugin;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class PlaceholderManager {
  private final Map<String, Placeholder> placeholders = new HashMap<>();
  private final GroupsPlugin plugin;

  public PlaceholderManager(GroupsPlugin plugin) {
    this.plugin = plugin;
    this.addDefaultPlaceholders();
  }

  private void addDefaultPlaceholders() {
    this.registerPlaceholder("{player_name}", HumanEntity::getName);
    this.registerPlaceholder(
        "{player_group_prefix}",
        player -> this.plugin.getGroupManager().getGroup(player).getPrefix());
    this.registerPlaceholder(
        "{player_group_name}", player -> this.plugin.getGroupManager().getGroup(player).getName());
  }

  public void registerPlaceholder(String placeholder, Placeholder replacer) {
    this.placeholders.put(placeholder, replacer);
  }

  public String resolvePlaceholders(String message, Player player) {
    for (Map.Entry<String, Placeholder> entry : this.placeholders.entrySet()) {
      String placeholder = entry.getKey();
      if (message.contains(placeholder)) {
        String replacement = entry.getValue().replace(player);
        message = message.replace(placeholder, replacement);
      }
    }
    return ChatColor.translateAlternateColorCodes('&', message);
  }
}
