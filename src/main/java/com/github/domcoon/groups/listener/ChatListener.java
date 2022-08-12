package com.github.domcoon.groups.listener;

import com.github.domcoon.groups.GroupsPlugin;
import com.github.domcoon.groups.configuration.ConfigBean;
import com.github.domcoon.groups.placeholders.PlaceholderManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
  private final PlaceholderManager placeholderManager;
  private final ChatBean config;

  public ChatListener(GroupsPlugin plugin, PlaceholderManager placeholderManager) {
    this.placeholderManager = placeholderManager;

    this.config = new ChatBean();
    plugin.getPluginConfiguration().addBeans(config);
    plugin.getPluginConfiguration().reloadConfiguration();
  }

  @EventHandler
  public void onChat(AsyncPlayerChatEvent event) {
    if (!config.isEnabled()) {
      return;
    }

    String newFormat = config.newFormat;
    newFormat = this.placeholderManager.resolvePlaceholders(newFormat, event.getPlayer());

    event.setFormat(newFormat);
  }

  public static class ChatBean implements ConfigBean {
    private boolean enabled;
    private String newFormat;

    @Override
    public String getSection() {
      return "chat";
    }

    @Override
    public void loadConfiguration(ConfigurationSection cs) {
      if (cs == null) {
        return;
      }

      this.enabled = cs.getBoolean("enabled");
      this.newFormat = cs.getString("format");
    }

    public boolean isEnabled() {
      return enabled;
    }
  }
}
