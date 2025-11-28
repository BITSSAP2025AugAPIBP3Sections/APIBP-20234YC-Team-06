package com.privychat.model.input;

public class CreateUserInput {
    private String username;
    private String password;
    private String publicKey;
    private String avatarUrl;

    public CreateUserInput() {}

    public CreateUserInput(String username, String password, String publicKey, String avatarUrl) {
        this.username = username;
        this.password = password;
        this.publicKey = publicKey;
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPublicKey() { return publicKey; }
    public void setPublicKey(String publicKey) { this.publicKey = publicKey; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}

