package com.github.dschreid.groups.sign;

import com.github.dschreid.groups.GroupsPlugin;
import com.github.dschreid.groups.events.PluginReloadedEvent;
import com.github.dschreid.groups.lang.LangKeys;
import com.github.dschreid.groups.lang.Message;
import com.github.dschreid.groups.lang.MessageFactory;
import com.github.dschreid.groups.lang.messagesimpl.ChatMessage;
import com.google.common.eventbus.Subscribe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class SignManager {
    private final String GROUP_SIGN_KEY = "group-sign"; // Will only affect signs containing this key

    private final GroupsPlugin plugin;
    private final MessageFactory messageFactory;
    private final Set<Location> signs;
    private final SignConfigBean config;
    private final Map<UUID, StoredSign> storedPlayerSigns;
    private SignUpdater signUpdater;

    public SignManager(GroupsPlugin plugin, MessageFactory messageFactory) {
        this.plugin = plugin;
        this.messageFactory = messageFactory;
        this.signs = new HashSet<>();
        this.storedPlayerSigns = new HashMap<>();
        this.config = new SignConfigBean();
        this.plugin.getPluginConfiguration().addBeans(this.config);
        this.plugin.getPluginConfiguration().subscribe(this);

        Bukkit.getPluginManager().registerEvents(new SignListener(this, config), plugin);
    }

    @Subscribe
    public void onReload(PluginReloadedEvent ignored) {
        if (this.signUpdater != null) {
            this.signUpdater.cancel();
        }
        this.signUpdater = new SignUpdater(this);
        this.signUpdater.runTaskTimer(plugin, 20, config.getUpdateTickSpeed());
    }

    public void sendUpdate(Player player) {
        if (!config.isEnabled()) {
            return;
        }

        Location location = player.getLocation();
        for (Location sign : this.signs) {
            if (!isSign(sign)) {
                plugin.getLogger().info("Removing Sign due to it no longer being a sign");
                this.signs.remove(sign);
                continue;
            }
            if (location.getWorld() != sign.getWorld()) {
                continue;
            }
            if (location.distance(sign) <= config.getUpdateRadius()) {
                StoredSign stored =
                        this.storedPlayerSigns.computeIfAbsent(
                                player.getUniqueId(),
                                (ignored) -> {
                                    String[] strings = this.generateLines(player);
                                    return new StoredSign(strings);
                                });
                player.sendSignChange(sign, stored.getContent());
            }
        }
    }

    public boolean isSign(Location sign) {
        return sign.getBlock().getType().name().contains("SIGN");
    }

    private String[] generateLines(Player player) {
        plugin.getLogger().info("Generating Lines for player");
        String[] result = {"", "", "", ""};
        Collection<Message> messagesRaw =
                this.messageFactory.getMessagesRaw(player, LangKeys.SIGN_TEMPLATE);
        ChatMessage msg = null;

        for (Message message : messagesRaw) {
            if (message instanceof ChatMessage) {
                msg = (ChatMessage) message;
            }
        }

        if (msg == null) {
            return result;
        }

        String content = msg.getContent();
        String[] lines = content.split("\n");
        if (lines.length == 0) {
            return result;
        }

        for (int i = 0; i < 4; i++) {
            if (lines.length > i) {
                result[i] = this.plugin.getPlaceholderManager().resolvePlaceholders(lines[i], player);
            }
        }
        return result;
    }

    public void removeSign(Location location) {
        if (this.signs.contains(location)) {
            plugin.getLogger().info("Removed Custom Sign");
            this.signs.remove(location);
        }
    }

    public boolean isCustomSign(String[] lines) {
        for (String line : lines) {
            if (line.contains(GROUP_SIGN_KEY)) {
                return true;
            }
        }
        return false;
    }

    public void addSign(Block block) {
        plugin.getLogger().info("Created Custom Sign");
        this.signs.add(block.getLocation());
    }

    public void invalidateStorage() {
        plugin.getLogger().info("Invalidating Sign Storage due to a permission change");
        this.storedPlayerSigns.clear();
    }
}
