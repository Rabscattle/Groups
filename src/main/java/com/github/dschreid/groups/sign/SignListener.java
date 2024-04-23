package com.github.dschreid.groups.sign;

import com.github.dschreid.groups.events.LanguageChangeEvent;
import com.github.dschreid.groups.events.PermissionChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

public class SignListener implements Listener {
    private final SignManager signManager;
    private final SignConfigBean config;

    public SignListener(SignManager signManager, SignConfigBean config) {
        this.signManager = signManager;
        this.config = config;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSign(SignChangeEvent event) {
        if (!config.isEnabled()) {
            return;
        }
        if (signManager.isCustomSign(event.getLines())) {
            signManager.addSign(event.getBlock());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSignBreak(BlockBreakEvent event) {
        if (!config.isEnabled()) {
            return;
        }
        if (!event.getBlock().getType().name().contains("SIGN")) {
            return;
        }
        this.signManager.removeSign(event.getBlock().getLocation());
    }

    @EventHandler
    public void onPermissionChange(PermissionChangeEvent event) {
        if (!config.isEnabled()) {
            return;
        }
        this.signManager.invalidateStorage();
    }

    @EventHandler
    public void onLanguageChange(LanguageChangeEvent event) {
        if (!config.isEnabled()) {
            return;
        }
        this.signManager.invalidateStorage();
    }
}
