package org.discordBot.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class UserTable {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;
    @Column(nullable = false,unique = true)
    private String userName;
    @Column(nullable = false)
    private LocalDateTime registrationTime;

    private String avatarUrl;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
