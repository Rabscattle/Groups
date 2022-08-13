package com.github.domcoon.groups.scoreboard;

import com.github.domcoon.groups.GroupsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ScoreboardManager {
  private final ScoreboardConfigBean config;
  private final GroupsPlugin plugin;
  private final ScoreboardFactory factory;

  public ScoreboardManager(GroupsPlugin plugin) {
    this.plugin = plugin;
    this.config = new ScoreboardConfigBean();
    this.factory = new JScoreboardFactory(plugin, config);
    this.plugin.getPluginConfiguration().addBeans(config);
    Bukkit.getPluginManager().registerEvents(new ScoreboardListener(this, config), plugin);
  }

  public void addPlayer(Player player) {
    factory.createScoreboard(player);
  }

  public void invalidateScoreboards() {
    factory.invalidateScoreboard();
  }
}
