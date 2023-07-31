package org.discordBot.service;

import org.discordBot.model.CastleTable;
import org.discordBot.model.HeroesTable;
import org.discordBot.repository.SessionJDBC;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomHeroAndCastleService {
    private final List<CastleTable> castles;
    SessionJDBC sessionJDBC;
    public RandomHeroAndCastleService() {
        castles = new ArrayList<>();
    }

    public String randomCastle(){
        Random random = new Random();
        SessionJDBC sessionJDBC = new SessionJDBC();
        castles.addAll(sessionJDBC.getAllCastles());
        int r =random.nextInt(castles.size()-1);
        return castles.get(r).getName();
    }
    public String findHeroesByCastleId(){
        StringBuilder str = new StringBuilder();
        SessionJDBC sessionJDBC = new SessionJDBC();
        List<HeroesTable> allHeroes = sessionJDBC.getAllHeroes();
        Random random = new Random();
        int idFirstHero = random.nextInt(allHeroes.size()-1);
        int idSecondHero = random.nextInt(allHeroes.size()-1);
        while(true) {
            if (allHeroes.get(idFirstHero).getName()
                    .equalsIgnoreCase(allHeroes.get(idSecondHero).getName())) {
                idSecondHero = random.nextInt(allHeroes.size() - 1);
            }
            break;
        }
        str.append(allHeroes.get(idFirstHero).getCastleId().getName()).append(" ")
                .append(allHeroes.get(idFirstHero).getName())
                .append(" ").append(allHeroes.get(idSecondHero).getCastleId().getName())
                .append(" ").append(allHeroes.get(idSecondHero).getName());
        sessionJDBC.closeSessionAndTransaction();
        return str.toString();
    }
}
