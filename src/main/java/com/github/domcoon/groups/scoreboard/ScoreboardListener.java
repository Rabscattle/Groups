package com.github.domcoon.groups.scoreboard;

import com.github.domcoon.groups.events.PermissionChangeEvent;
import com.github.domcoon.groups.events.UserLoadedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ScoreboardListener implements Listener {
  private final ScoreboardManager manager;
  private final ScoreboardConfigBean config;

  public ScoreboardListener(ScoreboardManager manager, ScoreboardConfigBean config) {
    this.manager = manager;
    this.config = config;
  }

  @EventHandler
  public void userLoaded(UserLoadedEvent event) {
    if (!config.isEnabled()) {
      return;
    }
    manager.addPlayer(event.getPlayer());
  }

  @EventHandler
  public void onPermissionChange(PermissionChangeEvent event) {
    if (!config.isEnabled()) {
      return;
    }
    manager.invalidateScoreboards();
  }
}
