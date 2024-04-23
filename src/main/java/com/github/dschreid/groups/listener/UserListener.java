package com.github.dschreid.groups.listener;

import com.github.dschreid.groups.GroupsPlugin;
import com.github.dschreid.groups.lang.LangKeys;
import com.github.dschreid.groups.model.user.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class UserListener implements Listener {
    private final GroupsPlugin plugin;
    private final UserManager userManager;

    public UserListener(GroupsPlugin plugin, UserManager userManager) {
        this.plugin = plugin;
        this.userManager = userManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        userManager
                .prepareUser(event.getPlayer())
                .whenComplete((user, throwable) -> this.greet(event.getPlayer()));
    }

    private void greet(Player player) {
        // Scheduling next tick so the message appears at the bottom of the chat
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.sendLocalizedMessage(player, LangKeys.GREET);
            }
        }.runTask(plugin);
    }
}
