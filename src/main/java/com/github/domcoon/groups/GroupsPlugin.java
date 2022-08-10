package com.github.domcoon.groups;

import co.aikar.commands.PaperCommandManager;
import com.github.domcoon.groups.commands.GroupCommands;
import com.github.domcoon.groups.commands.RankCommand;
import com.github.domcoon.groups.commands.UserCommands;
import com.github.domcoon.groups.configuration.PluginConfiguration;
import com.github.domcoon.groups.lang.MessageFactory;
import com.github.domcoon.groups.listener.ChatListener;
import com.github.domcoon.groups.listener.UserListener;
import com.github.domcoon.groups.model.group.GroupManager;
import com.github.domcoon.groups.model.user.UserManager;
import com.github.domcoon.groups.placeholders.PlaceholderManager;
import com.github.domcoon.groups.placeholders.PlaceholderPair;
import com.github.domcoon.groups.storage.Storage;
import com.github.domcoon.groups.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class GroupsPlugin extends JavaPlugin {
    private PluginConfiguration pluginConfiguration;
    private MessageFactory messageFactory;
    private PaperCommandManager commandManager;
    private GroupManager groupManager;
    private UserManager userManager;
    private PlaceholderManager placeholderManager;
    private Storage storage;

    @Override
    public void onEnable() {
        this.pluginConfiguration = new PluginConfiguration(this);
        this.messageFactory = new MessageFactory(this);
        this.commandManager = new PaperCommandManager(this);
        this.storage = new Storage(this);
        this.groupManager = new GroupManager(this.storage, this);
        this.userManager = new UserManager(this.storage, this);
        this.placeholderManager = new PlaceholderManager(this);

        this.messageFactory.reloadLanguages();
        this.pluginConfiguration.reloadConfiguration();

        try {
            this.storage.init();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // UI
        registerCommands();
        registerListeners();
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new UserListener(this, this.userManager), this);
        pluginManager.registerEvents(new ChatListener(this, placeholderManager), this);
    }

    private void registerCommands() {
        this.commandManager.enableUnstableAPI("help");
        this.commandManager.registerCommand(new GroupCommands(this, groupManager));
        this.commandManager.registerCommand(new UserCommands(this, userManager));
        this.commandManager.registerCommand(new RankCommand(userManager, groupManager));
    }

    @Override
    public void onDisable() {
        this.storage.close();
    }

    public void sendLocalizedMessage(CommandSender sender, String message, PlaceholderPair... pairs) {
        this.messageFactory.sendMessage(sender, message, pairs);
    }

    public PluginConfiguration getPluginConfiguration() {
        return pluginConfiguration;
    }

    public UserManager getUserManager() {
        return this.userManager;
    }

    public GroupManager getGroupManager() {
        return this.groupManager;
    }
}
