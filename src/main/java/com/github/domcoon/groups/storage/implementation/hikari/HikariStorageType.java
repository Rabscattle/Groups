package com.github.domcoon.groups.storage.implementation.hikari;

import com.zaxxer.hikari.HikariConfig;

public enum HikariStorageType {
    MYSQL("com.mysql.cj.jdbc.Driver") {
        @Override
        public void fillConfig(HikariConfig config, String url, int port, String db, String user, String password) {
            config.setDriverClassName(getDriver());
            config.setJdbcUrl("jdbc:mysql://%s:%d/%s".formatted(url, port, db));
            config.setUsername(user);
            config.setPassword(password);
        }
    },
    MARIADB("org.mariadb.jdbc.MariaDbDataSource") {
        @Override
        public void fillConfig(HikariConfig config, String url, int port, String db, String user, String password) {
            config.setDataSourceClassName(getDriver());
            config.addDataSourceProperty("serverName", url);
            config.addDataSourceProperty("port", port);
            config.addDataSourceProperty("databaseName", db);
            config.setUsername(user);
            config.setPassword(password);
        }
    },
    ;

    private final String driver;

    HikariStorageType(String driver) {
        this.driver = driver;
    }

    protected String getDriver() {
        return this.driver;
    }

    public abstract void fillConfig(HikariConfig config, String url, int port, String db, String user, String password);
}
