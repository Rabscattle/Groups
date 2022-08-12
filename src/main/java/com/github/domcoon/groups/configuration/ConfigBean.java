package com.github.domcoon.groups.configuration;

import org.bukkit.configuration.ConfigurationSection;

public interface ConfigBean {
  String getSection();

  void loadConfiguration(ConfigurationSection cs);
}
