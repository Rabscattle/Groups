package com.github.domcoon.groups.lang.messagesimpl;

import com.github.domcoon.groups.lang.Message;
import com.github.domcoon.groups.placeholders.PlaceholderPair;
import com.github.domcoon.groups.util.PlaceholderUtil;
import com.google.common.base.Strings;
import org.bukkit.command.CommandSender;

public class ChatMessage implements Message {
  private final String messageTemplate;

  public ChatMessage(String messageTemplate) {
    this.messageTemplate = messageTemplate;
  }

  @Override
  public void sendMessage(CommandSender sender, PlaceholderPair[] values) {
    if (!Strings.isNullOrEmpty(messageTemplate)) {
      String message = PlaceholderUtil.replacePlaceholders(this.messageTemplate, values);
      sender.sendMessage(message);
    }
  }

  public String getContent() {
    return messageTemplate;
  }
}
