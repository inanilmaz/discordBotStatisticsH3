package org.discordBot.service;


import org.discordBot.model.CastleTable;
import org.discordBot.model.GameTable;
import org.discordBot.model.GlobalStatistics;
import org.discordBot.repository.SessionJDBC;
import org.discordBot.model.UserTable;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalStatisticsService {
    private List<GameTable> gamesWin;
    private List<GameTable> gamesLose;
    private final List<GameTable> allGames;
    private final SessionJDBC sessionJDBC;
    private long countAllGames;
    private Long countAllWinsGames;

    public GlobalStatisticsService() {
        allGames = new ArrayList<>();
        sessionJDBC = new  SessionJDBC();
    }
    private void addValueInLists(String name){
        int userId;
        List<UserTable> users = sessionJDBC.getAllUsers();
        for (UserTable user : users) {
            if (user.getUserName().equalsIgnoreCase(name)) {
                userId = user.getId();
                gamesWin = sessionJDBC.getGameByUserId(userId);
                gamesLose = sessionJDBC.getGameByRivalId(userId);
                allGames.addAll(gamesLose);
                allGames.addAll(gamesWin);
            }
        }
    }

    public GlobalStatistics getStatisticsByName(String name) {
        if(sessionJDBC.isRegistration(name)){
            CastleTable castleTable = findMostRepeatedCastleTable(name);
            int id = sessionJDBC.getIdByName(name).getId();
            countAllGames = sessionJDBC.getCountOfGamesByUserIdAndCastleId(id,castleTable.getId())
                    + sessionJDBC.getCountOfGamesByRivalIdAndRivalCastleId(id,castleTable.getId());
            countAllWinsGames = sessionJDBC.getCountOfGamesByUserIdAndCastleId(id,castleTable.getId());
            if(castleTable != null){
                addValueInLists(name);
                GlobalStatistics statisticsModel =
                        new GlobalStatistics(getWinRate(), allGames.size(), castleTable.getName(),
                                castleTable.getUrlImg(),countAllGames , calculateWinRate());
                sessionJDBC.closeSessionAndTransaction();
                return statisticsModel;
            }
        }
        return null;
    }

    private double calculateWinRate(){
        if (countAllGames == 0) {
            return 0.0;
        }
        double value = (double) (countAllWinsGames/countAllGames) * 100;
        MathContext context = new MathContext(4,RoundingMode.HALF_UP);
        BigDecimal result = new BigDecimal(value,context);
        return result.doubleValue();
    }
    private Double getWinRate(){
        double countWinGame = gamesWin.size();
        double allGame = allGames.size();
        if (countWinGame == 0 || allGame == 0 || Double.isInfinite(countWinGame / allGame)) {
            return 0.0;
        }
        double value = (countWinGame / allGame) * 100;
        MathContext context = new MathContext(4, RoundingMode.HALF_UP);
        BigDecimal result = new BigDecimal(value, context);
        return result.doubleValue();
    }
    public CastleTable findMostRepeatedCastleTable(String name) {
        Map<CastleTable, Integer> countMap = new HashMap<>();
        List<CastleTable> groupCastle = groupCastleTableInList(name);
        if(groupCastle != null){
            for(CastleTable item : groupCastle){
                countMap.put(item,countMap.getOrDefault(item,0)+1);
            }
            int maxCount = 0;
            CastleTable maxRepeatedCastleTable = null;
            for (Map.Entry<CastleTable, Integer> entry : countMap.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    maxRepeatedCastleTable = entry.getKey();
                }
            }
            return maxRepeatedCastleTable;
        }
        return null;

    }
    public List<CastleTable> groupCastleTableInList(String userName){
        List<GameTable> groupGames = new ArrayList<>();
        List<CastleTable> groupCastle = new ArrayList<>();
        int id = sessionJDBC.getIdByName(userName).getId();
        groupGames.addAll(sessionJDBC.getAllCastleInGameByUserId(id));
        groupGames.addAll(sessionJDBC.getAllRivalCastleGameByUserId(id));
        for(GameTable gt : groupGames){
            groupCastle.add(gt.getCastle());
        }
        return groupCastle;
    }
}
