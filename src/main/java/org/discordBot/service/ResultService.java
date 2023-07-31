package org.discordBot.service;

import org.discordBot.model.CastleTable;
import org.discordBot.repository.SessionJDBC;

import java.util.ArrayList;
import java.util.List;

public class ResultService {
    private final String[] addResultCommand;
    private final List<CastleTable> castles;
    private String firstCastle;
    private String secondCastle;
    private SessionJDBC sessionJDBC;
    public ResultService(String[] addResultCommand) {
        this.addResultCommand =addResultCommand;
        castles = new ArrayList<>();
        sessionJDBC = new SessionJDBC();
    }
    public boolean checkCastle(){
        SessionJDBC sessionJDBC = new SessionJDBC();
        castles.addAll(sessionJDBC.getAllCastles());
        for(CastleTable castle: castles){
            if(castle.getName().equalsIgnoreCase(addResultCommand[1])){
                firstCastle = castle.getName();
            }if(castle.getName().equalsIgnoreCase(addResultCommand[3])){
                secondCastle = castle.getName();
            }
        }
        return firstCastle != null && secondCastle != null;
    }
    public boolean mainCheck(){
        if(addResultCommand.length ==4 && checkCastle() && checkUser()){
            sessionJDBC.closeSessionAndTransaction();
            return true;
        }else {
            sessionJDBC.closeSessionAndTransaction();
            return false;
        }
    }
    public boolean checkUser(){
        return sessionJDBC.isRegistration(addResultCommand[0])
                && sessionJDBC.isRegistration(addResultCommand[2]);
    }
}