package com.github.domcoon.groups.sign;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SignUpdater extends BukkitRunnable {
    private final SignManager signManager;

    public SignUpdater(SignManager signManager) {
        this.signManager = signManager;
    }

    @Override
    public void run() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            this.signManager.sendUpdate(onlinePlayer);
        }
    }
}
