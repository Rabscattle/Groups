package com.github.domcoon.groups.lang;

import com.github.domcoon.groups.GroupsPlugin;
import com.github.domcoon.groups.placeholders.PlaceholderPair;
import com.github.domcoon.groups.util.FileUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
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

        String absolutePath = plugin.getDataFolder().getAbsolutePath() + "/languages";
        File file = new File(absolutePath);
        FileUtils.loadDirectory(file, false, this::loadFile);

        // Stats
        Logger logger = plugin.getLogger();
        logger.info("Loaded (%d) Languages".formatted(this.languages.size()));
        for (MessageStorage value : this.languages.values()) {
            logger.info(" - Language: (%s) with (%d Messages)".formatted(value.getLanguage(), value.getMessages().size()));
        }
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

        language.sendMessage(sender, message, values);
    }

    private MessageStorage getLanguage(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return languages.get(DEFAULT_LANGUAGE);
        }
        Player player = ((Player) sender);
        String language = player.getPersistentDataContainer().getOrDefault(languageKey, PersistentDataType.STRING, DEFAULT_LANGUAGE);
        return languages.containsKey(language) ? languages.get(language) : languages.get(DEFAULT_LANGUAGE);
    }
}
