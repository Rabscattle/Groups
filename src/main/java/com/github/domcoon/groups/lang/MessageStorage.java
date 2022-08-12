package com.github.domcoon.groups.lang;

import com.github.domcoon.groups.placeholders.PlaceholderPair;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class MessageStorage {
  private final String language;
  private final Map<String, Messages> messages = new HashMap<>();

  public MessageStorage(String language) {
    this.language = language;
  }

  public static MessageStorage fromConfig(String language, FileConfiguration fileConfiguration) {
    MessageStorage messageStorage = new MessageStorage(language);
    for (String key : fileConfiguration.getKeys(false)) {
      if (fileConfiguration.isConfigurationSection(key)) {
        messageStorage.messages.put(
            key, Messages.fromConfig(fileConfiguration.getConfigurationSection(key)));
      }
    }
    return messageStorage;
  }

  public void sendMessage(CommandSender sender, String messageKey, PlaceholderPair[] values) {
    if (!messages.containsKey(messageKey)) {
      sender.sendMessage(messageKey);
      return;
    }

    messages.get(messageKey).sendMessage(sender, values);
  }

  public String getLanguage() {
    return language;
  }

  public Map<String, Messages> getMessages() {
    return messages;
  }
}
