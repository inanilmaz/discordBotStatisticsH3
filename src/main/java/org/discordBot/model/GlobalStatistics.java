package org.discordBot.model;

public class GlobalStatistics {
    public GlobalStatistics(double winRate, int allMatches,
                            String favoriteCastle, String urlCastle,
                            Long matchesBestCastle, double winRateCastle) {
        this.winRate = winRate;
        this.allMatches = allMatches;
        this.favoriteCastle = favoriteCastle;
        this.urlCastle = urlCastle;
        this.matchesBestCastle = matchesBestCastle;
        this.winRateCastle = winRateCastle;
    }

    private double winRate;
    private int allMatches;
    private String favoriteCastle;
    private String urlCastle;
    private long matchesBestCastle;
    private double winRateCastle;

    public double getWinRateCastle() {
        return winRateCastle;
    }

    public void setWinRateCastle(double winRateCastle) {
        this.winRateCastle = winRateCastle;
    }

    public long getMatchesBestCastle() {
        return matchesBestCastle;
    }

    public void setMatchesBestCastle(int matchesBestCastle) {
        this.matchesBestCastle = matchesBestCastle;
    }

    public String getUrlCastle() {
        return urlCastle;
    }

    public void setUrlCastle(String urlCastle) {
        this.urlCastle = urlCastle;
    }

    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }

    public int getAllMatches() {
        return allMatches;
    }

    public void setAllMatches(int allMatches) {
        this.allMatches = allMatches;
    }

    public String getFavoriteCastle() {
        return favoriteCastle;
    }

    public void setFavoriteCastle(String favoriteCastle) {
        this.favoriteCastle = favoriteCastle;
    }
}
