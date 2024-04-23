package com.github.dschreid.groups.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LanguageChangeEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();
    private final Player player;
    private final String newLanguage;

    public LanguageChangeEvent(Player player, String newLanguage) {
        this.player = player;
        this.newLanguage = newLanguage;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public Player getPlayer() {
        return player;
    }

    public String getNewLanguage() {
        return newLanguage;
    }

    @Override
    public HandlerList getHandlers() {
        return LanguageChangeEvent.handlerList;
    }
}
