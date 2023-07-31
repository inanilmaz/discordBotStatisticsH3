package org.discordBot.model;

import jakarta.persistence.*;

@Entity
@Table(name = "games")
public class GameTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private UserTable user;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private CastleTable castle;
    @Enumerated(EnumType.STRING)
    private ResultGameEnum result;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private UserTable rival;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private CastleTable rivalCastle;

    public CastleTable getRivalCastle() {
        return rivalCastle;
    }

    public void setRivalCastle(CastleTable rivalCastle) {
        this.rivalCastle = rivalCastle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserTable getUser() {
        return user;
    }

    public void setUser(UserTable user) {
        this.user = user;
    }

    public CastleTable getCastle() {
        return castle;
    }

    public void setCastle(CastleTable castle) {
        this.castle = castle;
    }

    public ResultGameEnum getResult() {
        return result;
    }

    public void setResult(ResultGameEnum result) {
        this.result = result;
    }

    public UserTable getRival() {
        return rival;
    }

    public void setRival(UserTable rival) {
        this.rival = rival;
    }
}
