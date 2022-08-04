package com.github.domcoon.groups.view;

import com.github.domcoon.groups.model.group.Group;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public interface GroupView {
    void sendView(CommandSender sender, Collection<Group> groups);
}
