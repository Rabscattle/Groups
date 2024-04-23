package com.github.dschreid.groups.commands;

import com.github.dschreid.groups.GroupsPlugin;
import com.github.dschreid.groups.PrefixedException;
import com.github.dschreid.groups.lang.LangKeys;
import com.github.dschreid.groups.model.PermissionManager;
import com.github.dschreid.groups.placeholders.PlaceholderPair;
import com.github.dschreid.groups.util.DurationUtil;
import org.bukkit.command.CommandSender;

import java.time.format.DateTimeParseException;

/**
 * Helper Class providing a simple way to execute permission commands for both groups and users as
 * ACF (Command Framework) doesn't support command inheritance
 */
public class PermissionBaseCommands {

    public static void executeSetPermission(
            GroupsPlugin plugin,
            PermissionManager manager,
            CommandSender sender,
            String subject,
            String permission,
            boolean value,
            String duration) {
        try {
            long durationParsed = DurationUtil.parseDuration(duration);
            manager
                    .setPermission(subject, permission, value, durationParsed)
                    .whenComplete(
                            (unused, throwable) ->
                                    handleCompletion(
                                            throwable,
                                            plugin,
                                            sender,
                                            LangKeys.PERMISSION_SET,
                                            LangKeys.FAILURE_DURING_SET));
        } catch (PrefixedException ex) {
            plugin.sendLocalizedMessage(sender, ex.getMessage());
        } catch (DateTimeParseException ex) {
            plugin.sendLocalizedMessage(
                    sender, LangKeys.INVALID_DURATION, PlaceholderPair.of("{duration}", duration));
        }
    }

    public static void executeRemovePermission(
            GroupsPlugin plugin,
            PermissionManager manager,
            CommandSender sender,
            String subject,
            String permission) {
        try {
            manager
                    .removePermission(subject, permission)
                    .whenComplete(
                            (unused, throwable) ->
                                    handleCompletion(
                                            throwable,
                                            plugin,
                                            sender,
                                            LangKeys.PERMISSION_REMOVED,
                                            LangKeys.FAILURE_DURING_REMOVE));
        } catch (PrefixedException ex) {
            plugin.sendLocalizedMessage(sender, ex.getMessage());
        }
    }

    private static void handleCompletion(
            Throwable throwable,
            GroupsPlugin plugin,
            CommandSender sender,
            String completionMessage,
            String failureMessage) {
        if (throwable != null) {
            Throwable cause = throwable.getCause();
            if (cause instanceof PrefixedException) {
                plugin.sendLocalizedMessage(sender, cause.getMessage());
            } else {
                plugin.getLogger().severe(cause.getMessage());
                plugin.sendLocalizedMessage(sender, failureMessage);
            }
        } else {
            plugin.sendLocalizedMessage(sender, completionMessage);
        }
    }
}
