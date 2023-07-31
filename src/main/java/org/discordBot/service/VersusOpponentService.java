package org.discordBot.service;

import org.discordBot.model.GameTable;
import org.discordBot.model.VersusOppStatistics;
import org.discordBot.repository.SessionJDBC;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

public class VersusOpponentService {
    private SessionJDBC sessionJDBC;
    private int winGamesCount;
    private int loseGamesCount;
    private String bestCastle;
    public VersusOppStatistics getStatVsOpp(String nickName, String opp){
        sessionJDBC = new SessionJDBC();
        int userId = sessionJDBC.getIdByName(nickName).getId();
        int rivalId = sessionJDBC.getIdByName(opp).getId();
        List<GameTable> winGames =sessionJDBC.getGameTableByUserIdAndRivalId(userId,rivalId);
        List<GameTable> loseGames = sessionJDBC.getGameTableByUserIdAndRivalId(rivalId,userId);
        winGamesCount = winGames.size();
        loseGamesCount = loseGames.size();
        VersusOppStatistics vs = !winGames.isEmpty() ?
                getAndSetVersusOppStatistics(winGames.get(0),winGames,loseGames) : null;

        return vs;
    }
    public VersusOppStatistics getAndSetVersusOppStatistics(GameTable game
            , List<GameTable> winGames, List<GameTable> loseGames){
        VersusOppStatistics vs = new VersusOppStatistics();
        vs.setUrlOpp(game.getRival().getAvatarUrl());
        vs.setWinRate(calculateWinRate());
        vs.setBestCastle(calculateBestPair(winGames));
        vs.setWorstCastle(calculateOppCastle(winGames));
        vs.setRivalName(game.getRival().getUserName());
        vs.setNameUser(game.getUser().getUserName());
        vs.setUrlImageCastle(sessionJDBC.getCastleByName(bestCastle).getUrlImg());
        return vs;
    }
    private double calculateWinRate(){
        int allGames = winGamesCount+loseGamesCount;
        double value = (double) winGamesCount / allGames * 100;
        MathContext context = new MathContext(4, RoundingMode.HALF_UP);
        BigDecimal result = new BigDecimal(value,context);
        return result.doubleValue();
    }
    private String calculateBestPair(List<GameTable> winsGame){
        Map<String, Integer> pairCount = new HashMap<>();
        for(GameTable game : winsGame){
            String castleName = game.getCastle().getName();
            pairCount.put(castleName,pairCount.getOrDefault(castleName,0)+1);
        }
        int maxCount = 0;
        for(Map.Entry<String,Integer> entry : pairCount.entrySet()){
            String castleName = entry.getKey();
            int count = entry.getValue();
            if(count>maxCount){
                maxCount= count;
                bestCastle = castleName;
            }
        }
        return bestCastle;
    }
    private String calculateOppCastle(List<GameTable> winsGame){
        String oppCastle = "";
        Map<String, Integer> pairCount = new HashMap<>();
        for(GameTable game : winsGame){
            if(game.getCastle().getName().equalsIgnoreCase(bestCastle)){
                String castleName = game.getRivalCastle().getName();
                pairCount.put(castleName,pairCount.getOrDefault(castleName,0)+1);
            }
        }
        int maxCount = 0;
        for(Map.Entry<String,Integer> entry : pairCount.entrySet()){
            String castleName = entry.getKey();
            int count = entry.getValue();
            if(count>maxCount){
                maxCount = count;
                oppCastle = castleName;
            }
        }
        return oppCastle;
    }
}
