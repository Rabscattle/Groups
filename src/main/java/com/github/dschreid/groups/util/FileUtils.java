package com.github.dschreid.groups.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileFilter;
import java.util.Objects;

public class FileUtils {
    public static void loadDirectory(
            final File dir, final boolean deep, final FileFilter filter, final FileHandler handler) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        for (final File file :
                Objects.requireNonNull(
                        dir.listFiles(f -> (deep && f.isDirectory()) || (f.isFile() && filter.accept(f))))) {
            if (file.isFile()) {
                handler.handle(file);
            } else if (file.isDirectory()) {
                loadDirectory(file, deep, filter, handler);
            }
        }
    }

    public static void loadDirectory(
            final File dir, final boolean deep, final FileConfigurationHandler handler) {
        loadDirectory(
                dir,
                deep,
                f -> f.getName().endsWith(".yml"),
                f -> handler.handle(f, YamlConfiguration.loadConfiguration(f)));
    }

    @FunctionalInterface
    public interface FileConfigurationHandler {
        void handle(final File file, final FileConfiguration config);
    }

    @FunctionalInterface
    public interface FileHandler {
        void handle(final File file);
    }
}
