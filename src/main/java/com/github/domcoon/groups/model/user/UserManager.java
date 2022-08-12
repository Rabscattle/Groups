package com.github.domcoon.groups.model.user;

import com.github.domcoon.groups.GroupsPlugin;
import com.github.domcoon.groups.PrefixedExceptionBuilder;
import com.github.domcoon.groups.events.PermissionChangeEvent;
import com.github.domcoon.groups.lang.LangKeys;
import com.github.domcoon.groups.model.AbstractManager;
import com.github.domcoon.groups.model.PermissionManager;
import com.github.domcoon.groups.model.group.Group;
import com.github.domcoon.groups.model.node.Node;
import com.github.domcoon.groups.model.node.NodeBuilder;
import com.github.domcoon.groups.storage.Storage;
import com.github.domcoon.groups.util.PermissionAssist;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.github.domcoon.groups.events.PermissionChangeEvent.*;

public class UserManager extends AbstractManager<UUID, User> implements PermissionManager {
    private final Storage storage;
    private final GroupsPlugin plugin;

    public UserManager(Storage storage, GroupsPlugin plugin) {
        this.storage = storage;
        this.plugin = plugin;
    }

    public CompletableFuture<User> prepareUser(Player player) {
        if (this.contains(player.getUniqueId()))
            return CompletableFuture.completedFuture(get(player.getUniqueId()));

        return storage.loadUser(player);
    }

    @Override
    protected User apply(UUID key) {
        return new User(key);
    }

    @Override
    public CompletableFuture<Void> setPermission(String subject, String permission, boolean value, long duration) {
        subject = subject.toLowerCase();
        Node build = new NodeBuilder(permission, value).duration(duration).build();

        return loadUser(subject).thenCompose(loaded -> {
            if (loaded == null) {
                throw new PrefixedExceptionBuilder().setMessage(LangKeys.USER_NOT_EXISTS).createPrefixedException();
            }
            return setPermission(loaded, build);
        });
    }

    public CompletableFuture<User> loadUser(UUID uuid) {
        if (contains(uuid))
            return CompletableFuture.completedFuture(get(uuid));

        return this.storage.loadUser(uuid);
    }

    public CompletableFuture<User> loadUser(String name) {
        User user = findByUserName(name);
        if (user != null)
            return CompletableFuture.completedFuture(user);

        return this.storage.loadUser(name);
    }

    private User findByUserName(String username) {
        for (User user : getAll()) {
            if (user.getUsername().equalsIgnoreCase(username))
                return user;
        }
        return null;
    }


    @Override
    public CompletableFuture<Void> removePermission(String subject, String permission) {
        return loadUser(subject).thenCompose(user -> {
            if (user == null)
                throw new PrefixedExceptionBuilder().setMessage(LangKeys.USER_NOT_EXISTS).createPrefixedException();
            return this.removePermission(user, permission);
        });
    }

    public CompletableFuture<Void> removePermission(User user, String node) {
        return PermissionAssist.removePermission(user, node)
                .thenCompose((ignored) -> this.storage.saveUser(user));
    }

    public CompletableFuture<Void> setPermission(User user, Node node) {
        return PermissionAssist.setPermission(user, node)
                .thenCompose((ignored) -> this.storage.saveUser(user));
    }

    public User getUser(Player player) {
        return this.get(player.getUniqueId());
    }

    public void invalidateAll() {
        getAll().forEach(User::invalidate);
    }
}
