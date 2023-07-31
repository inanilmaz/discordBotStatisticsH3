package org.discordBot.service;

import org.discordBot.model.CastleStatistics;
import org.discordBot.model.CastleTable;
import org.discordBot.repository.SessionJDBC;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CastleStatisticsService {
    SessionJDBC sessionJDBC = new SessionJDBC();

    long winsGames;
    long loseGames;
    long allGames;

    public List<CastleStatistics> getStats(String name){
        List<CastleTable> allCastles = sessionJDBC.getAllCastles();
        int id = sessionJDBC.getIdByName(name).getId();
        List<CastleStatistics> castleStats = new ArrayList<>();
        for(CastleTable castle : allCastles){
            winsGames = sessionJDBC.getCountOfGamesByUserIdAndCastleId(id,castle.getId());
            loseGames = sessionJDBC.getCountOfGamesByRivalIdAndRivalCastleId(id,castle.getId());
            allGames = winsGames+loseGames;
            castleStats.add(setCastleStatistics(castle,id));
        }
        sessionJDBC.closeSessionAndTransaction();
        return castleStats;
    }
    private CastleStatistics setCastleStatistics(CastleTable castle,int id){
        CastleStatistics castleStatistics = new CastleStatistics();
        castleStatistics.setCastle(castle.getName());
        castleStatistics.setAllGames(allGames);
        castleStatistics.setWinRate(getWinRate(id,castle));
        return castleStatistics;
    }
    private Double getWinRate(int id, CastleTable castle){
        if(winsGames != 0 || loseGames != 0){
            double value = ((double) winsGames / allGames) * 100;
            MathContext context = new MathContext(4, RoundingMode.HALF_UP);
            BigDecimal result = new BigDecimal(value, context);
            return result.doubleValue();
        }else {
            return 0.0;
        }

    }

}
