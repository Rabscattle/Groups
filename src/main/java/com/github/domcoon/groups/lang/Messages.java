package com.github.domcoon.groups.lang;

import com.github.domcoon.groups.lang.messagesimpl.ChatMessage;
import com.github.domcoon.groups.lang.messagesimpl.TitleMessage;
import com.github.domcoon.groups.placeholders.PlaceholderPair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class Messages {
  private final Collection<Message> messageCollection = new ArrayList<>();

  public static Messages fromConfig(ConfigurationSection section) {
    Messages messages = new Messages();
    if (section == null) {
      return messages;
    }

    if (section.contains("msg")) {
      messages.messageCollection.add(new ChatMessage(section.getString("msg")));
    }

    if (section.isConfigurationSection("title")) {
      messages.messageCollection.add(new TitleMessage(section.getConfigurationSection("title")));
    }

    return messages;
  }

  public void sendMessage(CommandSender sender, PlaceholderPair[] values) {
    for (Message message : messageCollection) {
      message.sendMessage(sender, values);
    }
  }

  public Collection<Message> getMessageCollection() {
    return Collections.unmodifiableCollection(messageCollection);
  }
}
