package com.github.domcoon.groups.util;

import com.github.domcoon.groups.placeholders.PlaceholderManager;
import com.github.domcoon.groups.placeholders.PlaceholderPair;
import java.util.List;
import org.bukkit.entity.Player;

public class PlaceholderUtil {

  public static String replacePlaceholders(String message, PlaceholderPair[] values) {
    for (PlaceholderPair value : values) {
      message = message.replace(value.getK(), value.getV());
    }
    return message;
  }

  public static List<String> replaceList(
      List<String> messages, Player player, PlaceholderManager manager) {
    messages.replaceAll(message -> manager.resolvePlaceholders(message, player));
    return messages;
  }
}
