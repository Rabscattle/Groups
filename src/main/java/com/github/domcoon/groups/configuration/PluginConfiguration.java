package com.github.domcoon.groups.configuration;

import com.github.domcoon.groups.GroupsPlugin;
import com.github.domcoon.groups.PrefixedException;
import com.github.domcoon.groups.events.PluginReloadedEvent;
import com.google.common.eventbus.EventBus;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents the config.yml
 * of a plugin. All sections of a yml are represented as a ConfigBean
 * and those are obligated to load from their corresponding section
 */
public class PluginConfiguration {
    private final List<ConfigBean> beans = new ArrayList<>();
    private final GroupsPlugin plugin;
    private final EventBus eventBus;

    public PluginConfiguration(GroupsPlugin plugin) {
        this.plugin = plugin;
        this.eventBus = new EventBus(plugin.getName());
    }

    public void reloadConfiguration() {
        this.plugin.saveDefaultConfig();
        this.plugin.reloadConfig();

        for (ConfigBean bean : this.beans) {
            try {
                bean.loadConfiguration(plugin.getConfig().getConfigurationSection(bean.getSection()));
            } catch (PrefixedException ex) {
                plugin.sendLocalizedMessage(Bukkit.getConsoleSender(), ex.getMessage());
            }
        }
        eventBus.post(new PluginReloadedEvent());
    }

    public void subscribe(Object... o) {
        for (Object o1 : o) {
            this.eventBus.register(o1);
        }
    }

    public void addBeans(ConfigBean... beans) {
        this.beans.addAll(Arrays.asList(beans));
    }

}
