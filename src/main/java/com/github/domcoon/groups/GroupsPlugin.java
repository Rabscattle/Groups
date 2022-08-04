package com.github.domcoon.groups;

import co.aikar.commands.PaperCommandManager;
import com.github.domcoon.groups.commands.group.GroupCommands;
import com.github.domcoon.groups.configuration.PluginConfiguration;
import com.github.domcoon.groups.model.group.GroupManager;
import com.github.domcoon.groups.storage.Storage;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class GroupsPlugin extends JavaPlugin {
    private PluginConfiguration pluginConfiguration;
    private PaperCommandManager commandManager;
    private GroupManager groupManager;
    private Storage storage;

    @Override
    public void onEnable() {
        this.pluginConfiguration = new PluginConfiguration(this);
        this.commandManager = new PaperCommandManager(this);
        this.storage = new Storage(this);
        this.groupManager = new GroupManager(this.storage);
        this.pluginConfiguration.reloadConfiguration();

        try {
            this.storage.init();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // UI
        registerCommands();
    }

    private void registerCommands() {
        this.commandManager.registerCommand(new GroupCommands(this, groupManager));
    }

    @Override
    public void onDisable() {
        this.storage.close();
    }

    public void sendLocalizedMessage(CommandSender sender, String message) {

    }

    public PluginConfiguration getPluginConfiguration() {
        return pluginConfiguration;
    }
}
