package com.github.domcoon.groups.commands;

import co.aikar.commands.BaseCommand;
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
import com.github.domcoon.groups.util.DurationUtil;
import com.github.domcoon.groups.view.GroupTextView;
import java.time.format.DateTimeParseException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("group")
@CommandPermission("groups.admin.groups")
public class GroupCommands extends BaseCommand {
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

  @Subcommand("assign")
  @Syntax("<player> <group> [expire]")
  public void assign(
      CommandSender sender, Player player, String group, @Single @Default("0s") String duration) {
    try {
      long expire = DurationUtil.parseDuration(duration);
      this.groupManager
          .assignGroup(player, group, expire)
          .whenComplete(
              (unused, throwable) ->
                  this.plugin.sendLocalizedMessage(sender, LangKeys.GROUP_ASSIGNED));
    } catch (PrefixedException ex) {
      this.plugin.sendLocalizedMessage(sender, ex.getMessage());
    } catch (DateTimeParseException ex) {
      this.plugin.sendLocalizedMessage(sender, LangKeys.INVALID_DURATION);
    }
  }

  @Subcommand("list")
  public void onView(CommandSender sender) {
    this.groupManager.sendGroupsView(sender, new GroupTextView());
  }

  @Subcommand("prefix")
  public class PrefixCommands extends BaseCommand {
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
  public class PermissionCommands extends BaseCommand {
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
