package com.github.domcoon.groups.scoreboard;

import com.github.domcoon.groups.GroupsPlugin;
import com.github.domcoon.groups.model.group.Group;
import com.github.domcoon.groups.util.PlaceholderUtil;
import dev.jcsoftware.jscoreboards.JPerPlayerScoreboard;
import dev.jcsoftware.jscoreboards.JScoreboardTeam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class JScoreboardFactory implements ScoreboardFactory {
  private final JPerPlayerScoreboard scoreboard;
  private final Map<String, JScoreboardTeam> groupToTeams;
  private final GroupsPlugin plugin;
  private final ScoreboardConfigBean config;

  public JScoreboardFactory(GroupsPlugin plugin, ScoreboardConfigBean config) {
    this.plugin = plugin;
    this.config = config;
    this.scoreboard = new JPerPlayerScoreboard(this::createHeader, this::createLines);
    this.groupToTeams = new HashMap<>();
  }

  private String createHeader(Player player) {
    String message = config.getHeader();
    return plugin.getPlaceholderManager().resolvePlaceholders(message, player);
  }

  private List<String> createLines(Player player) {
    List<String> rows = config.getRows();
    return PlaceholderUtil.replaceList(rows, player, plugin.getPlaceholderManager());
  }

  @Override
  public void createScoreboard(Player player) {
    scoreboard.addPlayer(player);
    Group group = plugin.getGroupManager().getGroup(player);

    JScoreboardTeam team =
        groupToTeams.computeIfAbsent(group.getName(), s -> this.createGroupBoard(group));
    team.addPlayer(player);

    team.refresh();
    scoreboard.updateScoreboard();
  }

  private JScoreboardTeam createGroupBoard(Group group) {
    return scoreboard.createTeam(
        "-%d.%s".formatted(group.getWeight(), group.getName()), group.getPrefix() + " ");
  }

  @Override
  public void invalidateScoreboard() {
    this.groupToTeams.clear();
    for (JScoreboardTeam team : new ArrayList<>(scoreboard.getTeams())) {
      scoreboard.removeTeam(team);
    }

    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      this.createScoreboard(onlinePlayer);
    }
  }
}
