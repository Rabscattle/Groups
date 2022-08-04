package com.github.domcoon.groups.commands.group;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import com.github.domcoon.groups.GroupsPlugin;
import com.github.domcoon.groups.PrefixedException;
import com.github.domcoon.groups.model.group.GroupManager;
import com.github.domcoon.groups.view.GroupTextView;
import org.bukkit.command.CommandSender;

@CommandAlias("groups")
@CommandPermission("groups.admin")
public class GroupCommands extends BaseCommand {
    private final GroupsPlugin plugin;
    private final GroupManager groupManager;

    public GroupCommands(GroupsPlugin plugin, GroupManager groupManager) {
        this.plugin = plugin;
        this.groupManager = groupManager;
    }

    @Subcommand("create")
    @Syntax("<name>")
    public void onCreate(CommandSender sender, String[] args) {
        String name = args[0];
        try {
            this.groupManager.createGroup(name);
        } catch (PrefixedException ex) {
            this.plugin.sendLocalizedMessage(sender, ex.getMessage());
        }
    }

    @Subcommand("list")
    public void onView(CommandSender sender, String[] args) {
        this.groupManager.sendGroupsView(sender, new GroupTextView());
    }
}
