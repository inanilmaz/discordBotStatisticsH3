package org.discordBot.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import org.discordBot.model.GlobalStatistics;
import org.discordBot.model.VersusOppStatistics;

import java.awt.*;


public class Embed {
    public EmbedBuilder buildEmbedCommands(){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(":tools: Команды:");
        eb.addField("","",false);
        eb.setColor(Color.CYAN);
        eb.addField(":first_place: /addresult", "Добавить результат вашей игры.",true);
        eb.addField(":first_place: /addvsrandom","Добавить результат игры с рандомом.",true);
        eb.addField(":open_hands: /mystats","Показывает вашу статистику.",true);
        eb.addField(":face_with_monocle: /statistics","Можно запросить статистику любого игрока.",true);
        eb.addField(":scream_cat: /castlestats","Показывает вашу статистику по каждому замку.",true);
        eb.addField(":face_with_monocle: /vsoppstats","Получить статистику против игрока.",true);
        eb.addField(":clipboard: /commands", "Узнать все комманды.",true);
        eb.addField(":identification_card: /registration", "Перед добавление в статистику необходимо зарегистрироваться.",true);
        eb.addField(":wave: /name", "Если забыли под каким никнеймом зарегестрированы пишите /start.", false);
        eb.addField(":game_die: /randomhero","Случайный выбор пары городов и героев.",true);
        eb.addField(":game_die: /randomcastle","Случайный выбор пары городов.",true);
        return eb;
    }
    public EmbedBuilder buildRandomHero(String twoHero){
        String[] heroes = twoHero.trim().split(" ");
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.CYAN);
        eb.addField(heroes[0], heroes[1], true);
        eb.addField("VS", "", true);
        eb.addField(heroes[2], heroes[3], true);
        eb.setImage("https://c4.wallpaperflare.com/wallpaper/205/953/68/artwork-fantasy-art-heroes-of-might-and-magic-heroes-of-might-and-magic-3-castle-hd-wallpaper-preview.jpg");
        return eb;
    }
    public EmbedBuilder buildRandomCastle(String firstCastle,String secondCastle){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.CYAN);
        eb.addField(firstCastle,"",true);
        eb.addField("VS","",true);
        eb.addField(secondCastle,"",true);
        eb.setImage("https://c4.wallpaperflare.com/wallpaper/394/939/687/artwork-fantasy-art-video-games-heroes-of-might-and-magic-heroes-of-might-and-magic-3-hd-wallpaper-preview.jpg");
        return eb;
    }
    public EmbedBuilder buildStatistics(GlobalStatistics statisticsModel){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(":page_with_curl: Статистика: ");
        eb.setColor(Color.CYAN);
        eb.addField(":video_game: Общее кол-во матчей",
                String.valueOf(statisticsModel.getAllMatches()),true);
        eb.addField(":medal: Win rate: ",
                statisticsModel.getWinRate() + "%",true);
        eb.addField(" "," ",false);
        eb.addField(":european_castle: Любимый город",
                statisticsModel.getFavoriteCastle(),true);
        eb.addField(":pinching_hand: Сыграно матчей за ",
                statisticsModel.getFavoriteCastle() + " : " +statisticsModel.getMatchesBestCastle() + " ",true);
        eb.addField(":medal: " + statisticsModel.getFavoriteCastle()
                , " Win Rate: " + statisticsModel.getWinRateCastle() + "%",true);
        eb.setImage(statisticsModel.getUrlCastle());
        return eb;
    }
    public EmbedBuilder buildUserStatistics(GlobalStatistics statisticsModel, String name){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(":page_with_curl: Статистика: " + name.toUpperCase());
        eb.setColor(Color.CYAN);
        eb.addField(":video_game: Общее кол-во матчей",
                String.valueOf(statisticsModel.getAllMatches()),true);
        eb.addField(":medal: Общий Win rate: ",
                statisticsModel.getWinRate() + "%",true);
        eb.addField(" "," ",false);
        eb.addField(":european_castle: Любимый город",
                statisticsModel.getFavoriteCastle(),true);
        eb.addField(":pinching_hand: Сыграно матчей за ",
                statisticsModel.getFavoriteCastle() + " : " +statisticsModel.getMatchesBestCastle() + " ",true);
        eb.addField(":medal: " + statisticsModel.getFavoriteCastle()
                , " Win Rate: " + statisticsModel.getWinRateCastle() + "%",true);
        eb.setImage(statisticsModel.getUrlCastle());
        return eb;
    }

    public EmbedBuilder buildVsOpp(VersusOppStatistics vs){
        EmbedBuilder eb = new EmbedBuilder();
        if(!vs.getUrlOpp().isEmpty()){
            eb.setThumbnail(vs.getUrlOpp());
        }
        eb.setTitle(":page_with_curl: Статистика VS " +vs.getRivalName().toUpperCase());
        eb.addField(":medal: Общий WinRate вашей пары : ", String.valueOf(vs.getWinRate()),false);
        eb.addField(":european_castle: Лучшая пара для тебя : ",vs.getBestCastle() + " VS "  + vs.getWorstCastle(),true);
        eb.setImage(vs.getUrlImageCastle());

        return eb;
    }
}
