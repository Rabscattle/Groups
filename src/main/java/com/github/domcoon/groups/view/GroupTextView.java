package com.github.domcoon.groups.view;

import com.github.domcoon.groups.model.group.Group;
import java.util.Collection;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class GroupTextView implements GroupView {
  @Override
  public void sendView(CommandSender sender, Collection<Group> groups) {
    for (Group group : groups) {
      sender.sendMessage(
          "%s%s (%d)".formatted(ChatColor.GREEN, group.getName(), group.getWeight()));
      group
          .getPermissionCache()
          .getAll()
          .forEach(
              node ->
                  sender.sendMessage(
                      "%s-- %s %s"
                          .formatted(ChatColor.BLUE, node.getPermission(), node.getValue())));
    }
  }
}
