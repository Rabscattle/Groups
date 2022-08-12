package com.github.domcoon.groups.view;

import com.github.domcoon.groups.model.group.Group;
import java.util.Collection;
import org.bukkit.command.CommandSender;

public interface GroupView {
  void sendView(CommandSender sender, Collection<Group> groups);
}
