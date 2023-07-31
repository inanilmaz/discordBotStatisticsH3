package org.discordBot.model;

public class CastleStatistics {
    private long allGames;
    private String castle;
    private double winRate;

    public long getAllGames() {
        return allGames;
    }

    public void setAllGames(long allGames) {
        this.allGames = allGames;
    }

    public String getCastle() {
        return castle;
    }

    public void setCastle(String castle) {
        this.castle = castle;
    }

    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }
}
