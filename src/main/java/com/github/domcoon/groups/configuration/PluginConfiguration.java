package com.github.domcoon.groups.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PluginConfiguration {
    private final List<ConfigBean> beans = new ArrayList<>();
    private final Plugin plugin;

    public PluginConfiguration(Plugin plugin) {
        this.plugin = plugin;
    }

    public void reloadConfiguration() {
        this.plugin.saveDefaultConfig();
        this.plugin.reloadConfig();

        for (ConfigBean bean : this.beans) {
            bean.loadConfiguration(plugin.getConfig().getConfigurationSection(bean.getSection()));
        }
    }

    public void addBeans(ConfigBean... beans) {
        this.beans.addAll(Arrays.asList(beans));
    }
}
