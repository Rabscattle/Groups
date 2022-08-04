package com.github.domcoon.groups.model.group;

import com.github.domcoon.groups.PrefixedException;
import com.github.domcoon.groups.lang.Lang;
import com.github.domcoon.groups.storage.Storage;
import com.github.domcoon.groups.view.GroupView;
import org.bukkit.command.CommandSender;

import java.util.concurrent.CompletableFuture;

public class GroupManager {
    public static final String DEFAULT_GROUP = "default";

    private final Storage storage;

    public GroupManager(Storage storage) {
        this.storage = storage;
    }

    public CompletableFuture<Group> createGroup(String name) {
        String key = name.toLowerCase();
        if (storage.groupExist(key)) {
            throw new PrefixedException(Lang.GROUP_EXIST);
        }
        return storage.createGroup(name);
    }

    public void sendGroupsView(CommandSender sender, GroupView view) {
        this.storage.getAllGroups().whenComplete((groups, throwable) -> {
            view.sendView(sender, groups);
        });
    }
}
