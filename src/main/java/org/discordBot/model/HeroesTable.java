package org.discordBot.model;

import jakarta.persistence.*;

@Entity
@Table(name = "heroes")
public class HeroesTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String urlImg;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private CastleTable castleId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public CastleTable getCastleId() {
        return castleId;
    }

    public void setCastleId(CastleTable castleId) {
        this.castleId = castleId;
    }

}
