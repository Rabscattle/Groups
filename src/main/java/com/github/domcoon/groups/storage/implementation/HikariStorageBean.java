package com.github.domcoon.groups.storage.implementation;

import com.github.domcoon.groups.configuration.ConfigBean;
import com.zaxxer.hikari.HikariConfig;
import org.bukkit.configuration.ConfigurationSection;

public class HikariStorageBean implements ConfigBean {
    private String url;
    private int port;
    private String pass;
    private String user;
    private String db;

    @Override
    public String getSection() {
        return "hikari-storage";
    }

    @Override
    public void loadConfiguration(ConfigurationSection cs) {
        if (cs == null)
            return;

        this.url = cs.getString("url", "");
        this.port = cs.getInt("port", 3306);
        this.user = cs.getString("user", "");
        this.pass = cs.getString("password", "");
        this.db = cs.getString("db", "");
    }

    public void fillHikariConfig(HikariConfig config) {
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://%s:%d/%s".formatted(url, port, db));
        config.setUsername(user);
        config.setPassword(pass);
    }
}
