package com.github.domcoon.groups.commands;

import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Single;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.annotation.Values;
import com.github.domcoon.groups.GroupsPlugin;
import com.github.domcoon.groups.PrefixedException;
import com.github.domcoon.groups.lang.LangKeys;
import com.github.domcoon.groups.model.group.GroupManager;
import com.github.domcoon.groups.view.GroupTextView;
import org.bukkit.command.CommandSender;

@CommandAlias("group")
@CommandPermission("groups.admin.groups")
public class GroupCommands extends ABaseCommand {
  private final GroupsPlugin plugin;
  private final GroupManager groupManager;

  public GroupCommands(GroupsPlugin plugin, GroupManager groupManager) {
    this.plugin = plugin;
    this.groupManager = groupManager;
  }

  @HelpCommand
  public void doHelp(CommandSender sender, CommandHelp help) {
    help.setPerPage(10);
    help.showHelp();
  }

  @Subcommand("create")
  @Syntax("<name>")
  public void create(CommandSender sender, String name) {
    try {
      this.groupManager.createAndLoad(name);
      this.plugin.sendLocalizedMessage(sender, LangKeys.GROUP_CREATED);
    } catch (PrefixedException ex) {
      this.plugin.sendLocalizedMessage(sender, ex.getMessage());
    }
  }

  @Subcommand("delete")
  @Syntax("<name>")
  public void delete(CommandSender sender, String name) {
    try {
      this.groupManager.deleteGroup(name);
      this.plugin.sendLocalizedMessage(sender, LangKeys.GROUP_DELETED);
    } catch (PrefixedException ex) {
      this.plugin.sendLocalizedMessage(sender, ex.getMessage());
    }
  }

  @Subcommand("list")
  public void onView(CommandSender sender) {
    this.groupManager.sendGroupsView(sender, new GroupTextView());
  }

  @Subcommand("weight")
  public class WeightCommands extends ABaseCommand {
    @Subcommand("set")
    @Syntax("<group> <weight>")
    public void set(CommandSender sender, String group, int weight) {
      try {
        groupManager.setWeight(group, weight);
        plugin.sendLocalizedMessage(sender, LangKeys.WEIGHT_SET);
      } catch (PrefixedException ex) {
        plugin.sendLocalizedMessage(sender, ex.getMessage());
      }
    }

    @Subcommand("add")
    @Syntax("<group> <weight> <prefix>")
    public void add(CommandSender sender, String group, int weight) {
      try {
        groupManager.addWeight(group, weight);
        plugin.sendLocalizedMessage(sender, LangKeys.WEIGHT_ADDED);
      } catch (PrefixedException ex) {
        plugin.sendLocalizedMessage(sender, ex.getMessage());
      }
    }

    @Subcommand("clear")
    @Syntax("<group>")
    public void clear(CommandSender sender, String group) {
      try {
        groupManager.clearWeight(group);
        plugin.sendLocalizedMessage(sender, LangKeys.WEIGHT_CLEARED);
      } catch (PrefixedException ex) {
        plugin.sendLocalizedMessage(sender, ex.getMessage());
      }
    }
  }

  @Subcommand("prefix")
  public class PrefixCommands extends ABaseCommand {
    @Subcommand("set")
    @Syntax("<group> <prefix>")
    public void setPrefix(CommandSender sender, String group, @Single String prefix) {
      try {
        groupManager.setPrefix(group, prefix);
        plugin.sendLocalizedMessage(sender, LangKeys.PREFIX_SET);
      } catch (PrefixedException ex) {
        plugin.sendLocalizedMessage(sender, ex.getMessage());
      }
    }

    @Subcommand("add")
    @Syntax("<group> <weight> <prefix>")
    public void addPrefix(CommandSender sender, String group, int weight, @Single String prefix) {
      try {
        groupManager.addPrefix(group, prefix, weight);
        plugin.sendLocalizedMessage(sender, LangKeys.PREFIX_ADDED);
      } catch (PrefixedException ex) {
        plugin.sendLocalizedMessage(sender, ex.getMessage());
      }
    }

    @Subcommand("clear")
    @Syntax("<group>")
    public void clearPrefix(CommandSender sender, String group) {
      try {
        groupManager.clearPrefix(group);
        plugin.sendLocalizedMessage(sender, LangKeys.PREFIX_CLEARED);
      } catch (PrefixedException ex) {
        plugin.sendLocalizedMessage(sender, ex.getMessage());
      }
    }
  }

  @Subcommand("perm|permission")
  public class PermissionCommands extends ABaseCommand {
    @Subcommand("set|add")
    @Syntax("<target> <permission> [true|false] [duration]")
    public void setPermission(
        CommandSender sender,
        String subject,
        String permission,
        @Values("true|false") @Default("true") boolean value,
        @Default("0s") @Single String duration) {
      PermissionBaseCommands.executeSetPermission(
          plugin, groupManager, sender, subject, permission, value, duration);
    }

    @Subcommand("unset|remove")
    @Syntax("<target> <permission>")
    public void removePermission(CommandSender sender, String subject, String permission) {
      PermissionBaseCommands.executeRemovePermission(
          plugin, groupManager, sender, subject, permission);
    }
  }
}
