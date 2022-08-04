package com.github.domcoon.groups.storage;

import com.github.domcoon.groups.GroupsPlugin;
import com.github.domcoon.groups.model.group.Group;
import com.github.domcoon.groups.storage.implementation.HikariStorage;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class Storage {
    private StorageImplementation implementation;

    public Storage(GroupsPlugin plugin) {
        this.implementation = new HikariStorage(plugin);
        this.implementation.preInit();
    }

    public void init() throws Exception {
        this.implementation.init();
    }

    public boolean groupExist(String key) {
        return false;
    }

    public CompletableFuture<Group> createGroup(String name) {
        return future(() -> implementation.createAndLoadGroup(name));
    }

    private <T> CompletableFuture<T> future(Callable<T> supplier) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return supplier.call();
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new CompletionException(e);
            }
        });
    }

    public void close() {
        this.implementation.close();
    }

    public CompletableFuture<Collection<Group>> getAllGroups() {
        return future(() -> this.implementation.getAllGroups());
    }
}
