package com.github.domcoon.groups.storage.implementation.hikari;

import com.github.domcoon.groups.PrefixedException;
import com.github.domcoon.groups.configuration.ConfigBean;
import com.zaxxer.hikari.HikariConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;

public class HikariStorageBean implements ConfigBean {
    private HikariStorageType type;
    private String url;
    private int port;
    private String pass;
    private String user;
    private String db;

    @Override
    public String getSection() {
        return "storage";
    }

    @Override
    public void loadConfiguration(ConfigurationSection cs) {
        if (cs == null)
            return;

        try {
            this.type = HikariStorageType.valueOf(cs.getString("type", "MARIADB"));
        } catch (Exception e) {
            throw new PrefixedException("Invalid Storage Type. Options are: %s".formatted(Arrays.toString(HikariStorageType.values())));
        }

        this.url = cs.getString("url", "");
        this.port = cs.getInt("port", 3306);
        this.user = cs.getString("user", "");
        this.pass = cs.getString("password", "");
        this.db = cs.getString("db", "");
    }

    public void fillHikariConfig(HikariConfig config) {
        this.type.fillConfig(config, url, port, db, user, pass);
    }
}
