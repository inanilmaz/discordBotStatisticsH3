package org.discordBot.events;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.discordBot.model.CastleStatistics;
import org.discordBot.model.GlobalStatistics;
import org.discordBot.model.VersusOppStatistics;
import org.discordBot.repository.SessionJDBC;
import org.discordBot.service.*;
import org.discordBot.utils.Embed;

import java.util.List;

import static org.discordBot.Main.updateChoices;


public class SlashCommand extends ListenerAdapter {
    private final RandomHeroAndCastleService randomHeroAndCastleService
            = new RandomHeroAndCastleService();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Embed embed = new Embed();
        switch (event.getName()) {
            case "name" -> event.reply(event.getUser().getName()).queue();
            case "commands" -> event.replyEmbeds(embed.buildEmbedCommands().build()).queue();
            case "randomhero" -> {
                event.deferReply().queue();
                String twoHero = randomHeroAndCastleService.findHeroesByCastleId();
                event.getHook().sendMessageEmbeds(embed.buildRandomHero(twoHero).build()).queue();
            }
            case "randomcastle" -> {
                event.deferReply().queue();
                String firstCastle = randomHeroAndCastleService.randomCastle();
                String secondCastle = randomHeroAndCastleService.randomCastle();
                while (true) {
                    if (firstCastle.equals(secondCastle)) {
                        secondCastle = randomHeroAndCastleService.randomCastle();
                        break;
                    }
                    break;
                }
                event.getHook().sendMessageEmbeds(embed.buildRandomCastle(firstCastle, secondCastle)
                        .build()).queue();
            }
            case "registration" -> {
                event.deferReply().queue();
                String userName = event.getUser().getName();
                SessionJDBC sessionJDBC = new SessionJDBC();
                String urlAvatar = event.getUser().getAvatar().getUrl();
                if (sessionJDBC.registrationUser(userName, urlAvatar)) {
                    event.getHook().sendMessage("Пользователь " + userName + " уже зарегистрирован").queue();
                    sessionJDBC.closeSessionAndTransaction();
                } else {
                    event.getHook().sendMessage("Вы зарегестрировались под ником " + userName).queue();
                    updateChoices();
                    sessionJDBC.closeSessionAndTransaction();
                }
            }
            case "mystats" -> {
                event.deferReply().queue();
                String userName = event.getUser().getName();
                GlobalStatisticsService statisticsService = new GlobalStatisticsService();
                GlobalStatistics statisticsModel = statisticsService.getStatisticsByName(userName);
                if (statisticsModel != null) {
                    event.getHook().sendMessageEmbeds(embed.buildStatistics(statisticsModel).build()).queue();
                } else {
                    event.getHook().sendMessage("Зарегистрируйтесь /registration");
                }
            }
            case "statistics" -> {
                event.deferReply().queue();
                String name = event.getOption("name").getAsString();
                GlobalStatisticsService statisticsService = new GlobalStatisticsService();
                GlobalStatistics statistics = statisticsService.getStatisticsByName(name);
                if (statistics != null) {
                    event.getHook().sendMessageEmbeds(embed.buildUserStatistics(statistics, name).build()).queue();
                } else {
                    event.getHook().sendMessage("Пользователь не зарегистрирован").submit();
                }

            }
            case "addresult" -> {
                event.deferReply().queue();
                OptionMapping winnerOption = event.getOption("winner");
                OptionMapping castleWinOption = event.getOption("castlewin");
                OptionMapping loserOption = event.getOption("loser");
                OptionMapping castleLoseOption = event.getOption("castlelose");
                if (winnerOption != null && castleWinOption != null &&
                        loserOption != null && castleLoseOption != null && !winnerOption.getAsString()
                        .equalsIgnoreCase(loserOption.getAsString())) {
                    String[] addResultCommand = new String[]{
                            winnerOption.getAsString(), castleWinOption.getAsString()
                            , loserOption.getAsString(), castleLoseOption.getAsString()
                    };
                    SessionJDBC sessionJDBC = new SessionJDBC();
                    event.getHook().sendMessage("Результат добавлен в статистику :thumbsup:").queue();
                    sessionJDBC.addResultInGameTable(addResultCommand);
                    sessionJDBC.closeSessionAndTransaction();
                } else {
                    event.getHook().sendMessage("Не правильная команда смотрите /commands").queue();
                }
            }
            case "addvsrandom" -> {
                event.deferReply().queue();
                String nickname = event.getUser().getName();
                OptionMapping castle = event.getOption("castle");
                OptionMapping castleOpp = event.getOption("castleopp");
                OptionMapping result = event.getOption("result");
                if (castle != null && castleOpp != null && result != null) {

                    String[] addResultCommand = new String[]{
                            nickname, castle.getAsString()
                            , castleOpp.getAsString(), result.getAsString()
                    };
                    SessionJDBC sessionJDBC = new SessionJDBC();
                    if (sessionJDBC.addResultWithOutOpp(addResultCommand)) {
                        event.getHook().sendMessage("Результат добавлен в статистику :thumbsup:").queue();
                        sessionJDBC.closeSessionAndTransaction();
                    } else {
                        event.getHook().sendMessage("Не правильная команда смотрите /commands").queue();
                        sessionJDBC.closeSessionAndTransaction();
                    }

                } else {
                    event.getHook().sendMessage("Не правильная команда смотрите /commands").queue();
                }
            }
            case "castlestats" -> {
                event.deferReply().queue();
                String nickname = event.getUser().getName();
                CastleStatisticsService castleStatisticsService = new CastleStatisticsService();
                List<CastleStatistics> castleStatistics = castleStatisticsService.getStats(nickname);
                StringBuilder builder = new StringBuilder();
                builder.append("```\n");
                builder.append(String.format("%-15s | %-12s | %s\n", "Замок", "Кол-во игр", "WinRate"));
                builder.append(String.format("%-15s | %-12s | %s\n", "---", "---", "---"));
                for (CastleStatistics statistics : castleStatistics) {
                    builder.append(String.format("%-15s | %-12d | %6.2f%%\n", statistics.getCastle(), statistics.getAllGames(), statistics.getWinRate()));
                }
                builder.append("```");
                event.getHook().sendMessage(builder.toString()).queue();
            }
            case "vsoppstats" -> {
                event.deferReply().queue();
                String nickname = event.getUser().getName();
                OptionMapping opp = event.getOption("opp");
                String opponent = opp.getAsString();
                if(nickname.equalsIgnoreCase(opponent)){
                    event.getHook().sendMessage("Вы указали свой ник!").queue();
                }else {
                    VersusOpponentService vs = new VersusOpponentService();
                    VersusOppStatistics vsStat = vs.getStatVsOpp(nickname,opponent);
                    event.getHook().sendMessageEmbeds(embed.buildVsOpp(vsStat).build()).queue();
                }
            }
        }
    }
}

