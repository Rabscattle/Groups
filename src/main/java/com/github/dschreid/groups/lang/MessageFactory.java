package com.github.dschreid.groups.lang;

import com.github.dschreid.groups.GroupsPlugin;
import com.github.dschreid.groups.PrefixedException;
import com.github.dschreid.groups.lang.messagesimpl.ChatMessage;
import com.github.dschreid.groups.placeholders.PlaceholderPair;
import com.github.dschreid.groups.util.FileUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

public class MessageFactory {
    private static final String DEFAULT_LANGUAGE = "en";

    private final Map<String, MessageStorage> languages = new HashMap<>();
    private final GroupsPlugin plugin;
    private final NamespacedKey languageKey;

    public MessageFactory(GroupsPlugin plugin) {
        this.plugin = plugin;
        this.languageKey = new NamespacedKey(this.plugin, "language");
    }

    public void reloadLanguages() {
        this.languages.clear();

        File file = saveDefaults();
        FileUtils.loadDirectory(file, false, this::loadFile);
        // Stats
        Logger logger = plugin.getLogger();
        logger.info("Loaded (%d) Languages".formatted(this.languages.size()));
        for (MessageStorage value : this.languages.values()) {
            logger.info(
                    " - Language: (%s) with (%d Messages)"
                            .formatted(value.getLanguage(), value.getMessages().size()));
        }
    }

    private File saveDefaults() {
        String absolutePath = plugin.getDataFolder().getAbsolutePath() + "/languages";
        File file = new File(absolutePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        this.plugin.saveResource("languages/messages-en.yml", false);
        return file;
    }

    private void loadFile(File file, FileConfiguration fileConfiguration) {
        String name = file.getName();
        if (!name.startsWith("messages-")) {
            return;
        }
        String language = name.substring("messages-".length(), name.length() - ".yml".length());
        MessageStorage messages = MessageStorage.fromConfig(language, fileConfiguration);
        this.languages.put(language, messages);
    }

    public void sendMessage(CommandSender sender, String message, PlaceholderPair... values) {
        MessageStorage language = getLanguage(sender);
        if (language == null) {
            sender.sendMessage(message);
            return;
        }

        if (sender instanceof Player && (values == null || values.length == 0)) {
            values = this.plugin.getPlaceholderManager().getPlaceholders(((Player) sender));
        }

        language.sendMessage(sender, message, values);
    }

    private MessageStorage getLanguage(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return languages.get(DEFAULT_LANGUAGE);
        }
        Player player = ((Player) sender);
        String language =
                player
                        .getPersistentDataContainer()
                        .getOrDefault(languageKey, PersistentDataType.STRING, DEFAULT_LANGUAGE);
        return languages.containsKey(language)
                ? languages.get(language)
                : languages.get(DEFAULT_LANGUAGE);
    }

    public void setLanguage(Player player, String language) {
        String keyed = language.toLowerCase();
        if (!this.languages.containsKey(keyed)) {
            throw new PrefixedException(LangKeys.LANGUAGE_NOT_EXISTS);
        }

        player.getPersistentDataContainer().set(languageKey, PersistentDataType.STRING, keyed);
    }

    public Collection<Message> getMessagesRaw(CommandSender sender, String key) {
        MessageStorage language = this.getLanguage(sender);
        List<Message> defaultReturn = Collections.singletonList(new ChatMessage(key));
        if (language == null) {
            return defaultReturn;
        }

        Messages messages = language.getMessages().get(key);
        if (messages == null) {
            return defaultReturn;
        }

        return messages.getMessageCollection();
    }
}
