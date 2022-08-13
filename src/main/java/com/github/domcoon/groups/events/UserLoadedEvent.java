package com.github.domcoon.groups.events;

import com.github.domcoon.groups.model.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a user is initially loaded into the memory
 */
public class UserLoadedEvent extends Event {
  private static final HandlerList handlerList = new HandlerList();
  private final Player player;
  private final User user;

  public UserLoadedEvent(Player player, User user) {
    this.player = player;
    this.user = user;
  }


  public static HandlerList getHandlerList() {
    return handlerList;
  }

  public Player getPlayer() {
    return player;
  }

  public User getUser() {
    return user;
  }

  @Override
  public HandlerList getHandlers() {
    return UserLoadedEvent.handlerList;
  }
}
