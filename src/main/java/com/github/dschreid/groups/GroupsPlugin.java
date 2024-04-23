package com.github.dschreid.groups;

import co.aikar.commands.PaperCommandManager;
import com.github.dschreid.groups.commands.*;
import com.github.dschreid.groups.configuration.PluginConfiguration;
import com.github.dschreid.groups.lang.MessageFactory;
import com.github.dschreid.groups.listener.ChatListener;
import com.github.dschreid.groups.listener.UserListener;
import com.github.dschreid.groups.model.group.GroupManager;
import com.github.dschreid.groups.model.user.UserManager;
import com.github.dschreid.groups.placeholders.PlaceholderManager;
import com.github.dschreid.groups.placeholders.PlaceholderPair;
import com.github.dschreid.groups.sign.SignManager;
import com.github.dschreid.groups.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class GroupsPlugin extends JavaPlugin {
    private static GroupsPlugin instance;
    private PluginConfiguration pluginConfiguration;
    private MessageFactory messageFactory;
    private PaperCommandManager commandManager;
    private GroupManager groupManager;
    private UserManager userManager;
    private PlaceholderManager placeholderManager;
    private SignManager signManager;
    private Storage storage;

    public static GroupsPlugin getInstance() {
        return instance;
    }

    private static void setInstance(GroupsPlugin instance) {
        GroupsPlugin.instance = instance;
    }

    @Override
    public void onEnable() {
        this.pluginConfiguration = new PluginConfiguration(this);
        this.messageFactory = new MessageFactory(this);
        this.commandManager = new PaperCommandManager(this);
        this.storage = new Storage(this);
        this.groupManager = new GroupManager(this.storage, this);
        this.userManager = new UserManager(this.storage, this);
        this.placeholderManager = new PlaceholderManager(this);
        this.signManager = new SignManager(this, messageFactory);
        this.messageFactory.reloadLanguages();
        this.pluginConfiguration.reloadConfiguration();

        try {
            this.storage.init();
        } catch (Exception e) {
            this.getLogger().severe(e.getMessage());
            this.getLogger().severe("DISABLING PLUGIN");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // UI
        setInstance(this);
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
        this.commandManager.registerCommand(new PluginCommands(this));
        this.commandManager.registerCommand(new GroupCommands(this, groupManager));
        this.commandManager.registerCommand(new UserCommands(this, userManager));
        this.commandManager.registerCommand(new RankCommand(this, userManager, groupManager));
        this.commandManager.registerCommand(new LanguageCommands(this, messageFactory));
    }

    @Override
    public void onDisable() {
        this.storage.close();
    }

    public void onReload() {
        this.messageFactory.reloadLanguages();
        this.pluginConfiguration.reloadConfiguration();

        this.storage.reload();
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

    public PlaceholderManager getPlaceholderManager() {
        return placeholderManager;
    }
}
