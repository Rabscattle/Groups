package com.github.domcoon.groups.scoreboard;

import org.bukkit.entity.Player;

public interface ScoreboardFactory {
  void createScoreboard(Player player);

  void invalidateScoreboard();
}
