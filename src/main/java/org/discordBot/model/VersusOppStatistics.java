package org.discordBot.model;

public class VersusOppStatistics {
    private String urlOpp;
    private double winRate;
    private String bestCastle;
    private String worstCastle;
    private String rivalName;
    private String nameUser;
    private String urlImageCastle;

    public String getWorstCastle() {
        return worstCastle;
    }

    public void setWorstCastle(String worstCastle) {
        this.worstCastle = worstCastle;
    }

    public String getUrlImageCastle() {
        return urlImageCastle;
    }

    public void setUrlImageCastle(String urlImageCastle) {
        this.urlImageCastle = urlImageCastle;
    }

    public String getBestCastle() {
        return bestCastle;
    }

    public void setBestCastle(String bestCastle) {
        this.bestCastle = bestCastle;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getRivalName() {
        return rivalName;
    }

    public void setRivalName(String rivalName) {
        this.rivalName = rivalName;
    }

    public String getUrlOpp() {
        return urlOpp;
    }

    public void setUrlOpp(String urlOpp) {
        this.urlOpp = urlOpp;
    }

    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }

}
