package com.github.domcoon.groups.model.group;

import com.github.domcoon.groups.GroupsPlugin;
import com.github.domcoon.groups.PrefixedExceptionBuilder;
import com.github.domcoon.groups.events.PermissionChangeEvent;
import com.github.domcoon.groups.lang.LangKeys;
import com.github.domcoon.groups.model.AbstractManager;
import com.github.domcoon.groups.model.PermissionManager;
import com.github.domcoon.groups.model.node.*;
import com.github.domcoon.groups.model.user.User;
import com.github.domcoon.groups.storage.Storage;
import com.github.domcoon.groups.util.PermissionAssist;
import com.github.domcoon.groups.view.GroupView;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;

import static com.github.domcoon.groups.events.PermissionChangeEvent.*;

public class GroupManager extends AbstractManager<String, Group> implements PermissionManager {
    public static final GroupNode DEFAULT_GROUP = new GroupNode("default", 0L);

    private final Storage storage;
    private final GroupsPlugin plugin;

    public GroupManager(Storage storage, GroupsPlugin plugin) {
        this.storage = storage;
        this.plugin = plugin;
        this.getOrCreate(DEFAULT_GROUP.getGroup());
    }

    @Override
    protected Group apply(String key) {
        return new Group(key);
    }

    @Override
    protected String normalize(String key) {
        return key.toLowerCase();
    }

    public CompletableFuture<Group> createAndLoad(String name) {
        String key = name.toLowerCase();
        if (contains(key)) {
            throw new PrefixedExceptionBuilder().setMessage(LangKeys.GROUP_EXIST).createPrefixedException();
        }
        return storage.createAndLoadGroup(name);
    }

    public void sendGroupsView(CommandSender sender, GroupView view) {
        view.sendView(sender, this.getAll());
    }

    public CompletableFuture<Void> assignGroup(Player player, String group, long expire) {
        User user = this.plugin.getUserManager().get(player.getUniqueId());
        return this.assignGroup(user, group, expire);
    }

    public CompletableFuture<Void> assignGroup(User user, String group, long duration) {
        GroupNode storedGroup = user.getStoredGroup();
        if (storedGroup != null && !storedGroup.isExpired() && group.equalsIgnoreCase(storedGroup.getGroup())) {
            throw new PrefixedExceptionBuilder().setMessage(LangKeys.GROUP_ALREADY_HAS).createPrefixedException();
        }

        if (!contains(group)) {
            throw new PrefixedExceptionBuilder().setMessage(LangKeys.GROUP_DOES_NOT_EXIST).createPrefixedException();
        }

        GroupNode groupNode = new GroupNode(group);
        if (duration > 0)
            groupNode.withDuration(duration);

        return plugin.getUserManager().setPermission(user, groupNode.toPermissionNode());
    }

    public Group getGroup(Player player) {
        User user = this.plugin.getUserManager().getUser(player);
        return this.getGroup(user);
    }

    public Group getGroup(User user) {
        return get(getGroupNode(user).getGroup());
    }

    public GroupNode getGroupNode(User user) {
        GroupNode storedGroup = user.getStoredGroup();
        if (storedGroup != null && contains(storedGroup.getGroup()) && !storedGroup.isExpired())
            return storedGroup;

        Collection<Node> nodes = user.getPermissionCache().getStartingWith("group.");
        if (nodes.isEmpty()) {
            user.setStoredGroup(DEFAULT_GROUP);
            return GroupManager.DEFAULT_GROUP;
        }

        GroupNode bestGroup = nodes.stream()
                .map(GroupNode::new)
                .filter(groupNode -> groupNode != DEFAULT_GROUP && contains(groupNode.getGroup()))
                .max(Comparator.comparingInt(this::getWeight))
                .orElse(DEFAULT_GROUP);

        user.setStoredGroup(bestGroup);
        return bestGroup;
    }

    private int getWeight(GroupNode node) {
        Group group = get(node.getGroup());
        return group == null ? -1 : group.getWeight();
    }

    @Override
    public CompletableFuture<Void> setPermission(String subject, String permission, boolean value, long expiring) {
        if (!contains(subject)) {
            throw new PrefixedExceptionBuilder().setMessage(LangKeys.GROUP_DOES_NOT_EXIST).createPrefixedException();
        }

        Group group = get(subject);
        Node build = new NodeBuilder(permission, value).duration(expiring).build();
        return setPermission(group, build);
    }

    public CompletableFuture<Void> setPermission(Group group, Node node) {
        return PermissionAssist.setPermission(group, node)
                .thenCompose((ignored) -> this.storage.saveGroup(group))
                .whenComplete((unused, throwable) -> this.plugin.getUserManager().invalidateAll());
    }

    @Override
    public CompletableFuture<Void> removePermission(String subject, String permission) {
        if (!contains(subject)) {
            throw new PrefixedExceptionBuilder().setMessage(LangKeys.GROUP_DOES_NOT_EXIST).createPrefixedException();
        }

        Group group = get(subject);
        return PermissionAssist.removePermission(group, permission)
                .thenCompose((ignored) -> this.storage.saveGroup(group));
    }

    public CompletableFuture<Void> deleteGroup(String name) {
        if (!contains(name)) {
            throw new PrefixedExceptionBuilder().setMessage(LangKeys.GROUP_DOES_NOT_EXIST).createPrefixedException();
        }
        remove(name);
        plugin.getUserManager().getAll().forEach(user -> {
            this.storage.removeNodeEverywhere("group.%s".formatted(name.toLowerCase()));
        });
        return storage.deleteGroup(name)
                .whenComplete((unused, throwable) -> {
                    if (DEFAULT_GROUP.getGroup().equalsIgnoreCase(name))
                        storage.createAndLoadGroup(GroupManager.DEFAULT_GROUP.getGroup());
                });
    }

    public void clearPrefix(String name) {
        if (!contains(name)) {
            throw new PrefixedExceptionBuilder().setMessage(LangKeys.GROUP_DOES_NOT_EXIST).createPrefixedException();
        }

        Group group = get(name);

        Collection<Node> matching = group.getPermissionCache().getMatching(PrefixNode.PREFIX_REGEX);
        matching.forEach(node -> {
            PermissionAssist.removePermission(group, node.getPermission());
        });
        group.invalidate();
    }

    /**
     * Adds the prefix to the existing table of prefixes^
     */
    public CompletableFuture<Void> addPrefix(String group, String prefix, int weight) {
        checkPrefix(prefix);
        Node prefixNode = new PrefixNode(weight, prefix).toPermissionNode();
        return setPermission(group, prefixNode.getPermission(), true, 0);
    }

    /**
     * Adds the prefix and clears all the previous prefixes
     *
     * @return
     */
    public CompletableFuture<Void> setPrefix(String group, String prefix) {
        checkPrefix(prefix);
        clearPrefix(group);
        return addPrefix(group, prefix, 0);
    }

    private void checkPrefix(String prefix) {
        if (prefix.length() < 1 || prefix.length() > 16) {
            throw new PrefixedExceptionBuilder().setMessage(LangKeys.INVALID_PREFIX).createPrefixedException();
        }
    }
}
