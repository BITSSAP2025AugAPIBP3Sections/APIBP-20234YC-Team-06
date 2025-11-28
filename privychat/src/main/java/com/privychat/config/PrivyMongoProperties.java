package com.privychat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.mongodb")
public class PrivyMongoProperties {
    /**
     * Full MongoDB connection URI. If present, it will be used directly.
     */
    private String uri;

    /**
     * Database name to use.
     */
    private String database;

    /**
     * Host, used when uri is not provided.
     */
    private String host = "localhost";

    /**
     * Port, used when uri is not provided.
     */
    private int port = 27017;

    /**
     * Username for authentication (used when uri is not provided).
     */
    private String username;

    /**
     * Password for authentication (used when uri is not provided).
     */
    private String password;

    /**
     * Authentication database (defaults to "admin").
     */
    private String authSource = "admin";

    // getters and setters
    public String getUri() { return uri; }
    public void setUri(String uri) { this.uri = uri; }

    public String getDatabase() { return database; }
    public void setDatabase(String database) { this.database = database; }

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAuthSource() { return authSource; }
    public void setAuthSource(String authSource) { this.authSource = authSource; }
}

