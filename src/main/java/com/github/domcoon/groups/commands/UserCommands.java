package com.github.domcoon.groups.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.github.domcoon.groups.GroupsPlugin;
import com.github.domcoon.groups.PrefixedException;
import com.github.domcoon.groups.lang.LangKeys;
import com.github.domcoon.groups.model.node.Node;
import com.github.domcoon.groups.model.user.User;
import com.github.domcoon.groups.model.user.UserManager;
import com.github.domcoon.groups.placeholders.PlaceholderPair;
import com.github.domcoon.groups.util.DurationUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("groups|g user|player")
@CommandPermission("groups.admin")
public class UserCommands extends BaseCommand {
    public final GroupsPlugin plugin;
    public final UserManager userManager;

    public UserCommands(GroupsPlugin plugin, UserManager userManager) {
        this.plugin = plugin;
        this.userManager = userManager;
    }

    @HelpCommand
    public void doHelp(CommandSender sender, CommandHelp help) {
        help.setPerPage(10);
        help.showHelp();
    }

    @Subcommand("info")
    @Syntax("[target]")
    public void info(CommandSender sender, @Optional String target) {
        if (!(sender instanceof Player) && target == null) {
            sender.sendMessage("You must specify a target");
            return;
        }

        if (target == null) {
            this.showInfo(sender, userManager.getUser((Player) sender));
            return;
        }

        this.userManager.loadUser(target).whenComplete((user, throwable) -> {
            if (throwable != null) {
                this.handleLoadingError(sender, throwable);
                return;
            }
            if (user != null)
                this.showInfo(sender, user);
            else
                this.plugin.sendLocalizedMessage(sender, LangKeys.USER_NOT_EXISTS);
        });
    }

    private void showInfo(CommandSender sender, User user) {
        sender.sendMessage("User: " + user.getUsername());
        sender.sendMessage("Group: " + this.plugin.getGroupManager().getGroup(user).getName());
        sender.sendMessage("Prefix: " + this.plugin.getGroupManager().getGroup(user).getPrefix());
        sender.sendMessage("Permissions:");
        for (Node node : user.getPermissionCache().getAll()) {
            sender.sendMessage(" - %s (%s)".formatted(node.getPermission(), DurationUtil.formatDuration(node.getTimeLeft(), "*")));
        }
    }

    private void handleLoadingError(CommandSender sender, Throwable throwable) {
        Throwable cause = throwable.getCause();
        if (cause instanceof PrefixedException) {
            plugin.sendLocalizedMessage(sender, cause.getMessage());
        } else
            plugin.sendLocalizedMessage(sender, LangKeys.FAILURE_DURING_LOAD);

    }

    @Subcommand("perm|permission")
    public class PermissionCommands extends BaseCommand {
        @Subcommand("set|add")
        @Syntax("<target> <permission> [true|false] [duration]")
        public void setPermission(CommandSender sender, String subject, String permission, @Values("true|false") @Default("true") boolean value, @Default("0s") @Single String duration) {
            PermissionBaseCommands.executeSetPermission(plugin,
                    userManager,
                    sender,
                    subject,
                    permission,
                    value,
                    duration);
        }

        @Subcommand("unset|remove")
        @Syntax("<target> <permission>")
        public void removePermission(CommandSender sender, String subject, String permission) {
            PermissionBaseCommands.executeRemovePermission(plugin,
                    userManager,
                    sender,
                    subject,
                    permission);
        }
    }
}
