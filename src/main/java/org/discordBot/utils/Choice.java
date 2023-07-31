package org.discordBot.utils;

import net.dv8tion.jda.api.interactions.commands.Command;
import org.discordBot.model.CastleTable;
import org.discordBot.model.UserTable;
import org.discordBot.repository.SessionJDBC;

import java.util.ArrayList;
import java.util.List;

public class Choice {
    public  List<Command.Choice> getCastleChoice(){
        SessionJDBC sessionJDBC = new SessionJDBC();
        List<Command.Choice> choices = new ArrayList<>();
        List<CastleTable> castles = sessionJDBC.getAllCastles();
        for(CastleTable castle : castles){
            choices.add(new Command.Choice(castle.getName(),castle.getName()));
        }
        sessionJDBC.closeSessionAndTransaction();
        return choices;
    }
    public  List<Command.Choice> getUserChoice(){
        SessionJDBC sessionJDBC = new SessionJDBC();
        List<Command.Choice> choices = new ArrayList<>();
        List<UserTable> users = sessionJDBC.getAllUsers();
        for(UserTable user : users){
            if(user.getUserName().equalsIgnoreCase("N/A")){
                continue;
            }
            choices.add(new Command.Choice(user.getUserName(),user.getUserName()));
        }
        sessionJDBC.closeSessionAndTransaction();
        return choices;
    }

}