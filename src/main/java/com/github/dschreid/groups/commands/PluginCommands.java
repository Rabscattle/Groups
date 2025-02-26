package com.github.dschreid.groups.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.github.dschreid.groups.GroupsPlugin;
import com.github.dschreid.groups.lang.LangKeys;
import org.bukkit.command.CommandSender;

@CommandAlias("groups|g")
@CommandPermission("groups.admin")
public class PluginCommands extends ABaseCommand {
    private final GroupsPlugin plugin;

    public PluginCommands(GroupsPlugin plugin) {
        this.plugin = plugin;
    }

    @Subcommand("reload")
    public void reload(CommandSender sender) {
        this.plugin.onReload();
        this.plugin.sendLocalizedMessage(sender, LangKeys.PLUGIN_RELOADED);
    }
}
