package com.github.domcoon.groups.scoreboard;

import com.github.domcoon.groups.configuration.ConfigBean;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;

public class ScoreboardConfigBean implements ConfigBean {
  private boolean enabled;

  private String header;
  private List<String> rows;

  @Override
  public String getSection() {
    return "scoreboard";
  }

  @Override
  public void loadConfiguration(ConfigurationSection cs) {
    if (cs == null) {
      return;
    }
    this.enabled = cs.getBoolean("enabled");
    this.header = cs.getString("header", "");
    this.rows = cs.getStringList("rows");
  }

  public boolean isEnabled() {
    return enabled;
  }

  public String getHeader() {
    return header;
  }

  public List<String> getRows() {
    return rows;
  }
}
