package com.github.domcoon.groups.commands;

import co.aikar.commands.BaseCommand;
import com.github.domcoon.groups.GroupsPlugin;
import com.github.domcoon.groups.PrefixedException;
import org.bukkit.command.CommandSender;

public class ABaseCommand extends BaseCommand {

  protected void handleCompletion(
      Throwable throwable, GroupsPlugin plugin, CommandSender sender, String completionMessage, String failureMessage) {
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
