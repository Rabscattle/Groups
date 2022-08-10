package com.github.domcoon.groups.storage;

import com.github.domcoon.groups.GroupsPlugin;
import com.github.domcoon.groups.PrefixedException;
import com.github.domcoon.groups.model.HolderType;
import com.github.domcoon.groups.model.group.Group;
import com.github.domcoon.groups.model.user.User;
import com.github.domcoon.groups.storage.implementation.hikari.HikariStorage;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class Storage {
    private final StorageImplementation implementation;

    public Storage(GroupsPlugin plugin) {
        this.implementation = new HikariStorage(plugin);
        this.implementation.preInit();
    }

    public CompletableFuture<Void> saveGroup(Group group) {
        return future(() -> this.implementation.saveGroup(group));
    }

    public CompletableFuture<Void> saveUser(User user) {
        return future(() -> this.implementation.saveUser(user));
    }

    public CompletableFuture<User> loadUser(String name) {
        return future(() -> this.implementation.loadUser(name));
    }

    public CompletableFuture<Void> deleteGroup(String name) {
        return future(() -> this.implementation.deleteGroup(name));
    }

    public CompletableFuture<User> loadUser(UUID uuid) {
        return future(() -> this.implementation.loadUser(uuid));
    }

    public CompletableFuture<Void> removeNodeEverywhere(String permission) {
        return future(() -> this.implementation.deleteNodesFromUsers(permission));
    }

    @FunctionalInterface
    interface Runnable {
        void run() throws Exception;
    }

    private CompletableFuture<Void> future(Runnable runnable) {
        return CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                System.out.println(e);
                throw new CompletionException(e);
            }
        });
    }

    private <T> CompletableFuture<T> future(Callable<T> supplier) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return supplier.call();
            } catch (Exception e) {
                System.out.println(e);
                throw new CompletionException(e);
            }
        });
    }

    public void init() throws Exception {
        this.implementation.init();

        this.implementation.deleteExpiredNodes(HolderType.GROUP);
        this.implementation.deleteExpiredNodes(HolderType.USER);
        this.implementation.loadAllGroups();
    }

    public void close() {
        this.implementation.close();
    }

    public CompletableFuture<Group> createAndLoadGroup(String name) {
        return future(() -> implementation.createAndLoadGroup(name));
    }

    public CompletableFuture<User> loadUser(Player player) {
        return  future(() -> this.implementation.loadUser(player.getUniqueId(), player.getName()));
    }

}
