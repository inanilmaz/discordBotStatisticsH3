package org.discordBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.discordBot.utils.Choice;
import org.discordBot.events.SlashCommand;

import java.util.List;

public class Main {
    private static final String token = "";
    private static JDA jda;

    public static void main(String[] args) {
        Choice choice = new Choice();
        jda = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new SlashCommand()).build();
        jda.updateCommands().addCommands(
                Commands.slash("name", "Твое имя."),
                Commands.slash("commands", "Все команды!"),
                Commands.slash("randomhero", "Рандом пары замков и героев."),
                Commands.slash("randomcastle", "Рандом пары замоков"),
                Commands.slash("registration", "Регистрация"),
                Commands.slash("mystats", "Статистика вашего аккаунта."),
                Commands.slash("statistics", "Ститистика по имени.")
                        .addOptions(List.of(
                                new OptionData(OptionType.STRING, "name", "Имя игрока", true)
                                        .addChoices(choice.getUserChoice()))),
                Commands.slash("addresult", "Добавить результаты в статистику.")
                        .addOptions(List.of(
                                new OptionData(OptionType.STRING,"winner","Ник победиля",true)
                                        .addChoices(choice.getUserChoice()),
                                new OptionData(OptionType.STRING, "castlewin", "Замок победителя.", true)
                                        .addChoices(choice.getCastleChoice()),
                                new OptionData(OptionType.STRING, "loser", "Ник проигравшего.", true)
                                        .addChoices(choice.getUserChoice()),
                                new OptionData(OptionType.STRING, "castlelose", "Замок проигравшего.", true)
                                        .addChoices(choice.getCastleChoice())
                        )),
                Commands.slash("addvsrandom", "Добавление в статистику с рандомом.")
                        .addOptions(List.of(
                                new OptionData(OptionType.STRING, "castle", "Ваш замок.")
                                        .setRequired(true)
                                        .addChoices(choice.getCastleChoice()),
                                new OptionData(OptionType.STRING, "castleopp", "Замок противника.")
                                        .setRequired(true)
                                        .addChoices(choice.getCastleChoice()),
                                new OptionData(OptionType.STRING, "result", "Результат игры.")
                                        .setRequired(true)
                                        .addChoices(new Command.Choice("WIN","win"),
                                                new Command.Choice("LOSE","lose"))

                        )),
                        Commands.slash("castlestats","Статистика твоих замков."),
                        Commands.slash("vsoppstats","Статистика против игрока.")
                                .addOptions(List.of(
                                        new OptionData(OptionType.STRING,"opp","Ник игрока против кого играл.")
                                                .setRequired(true)
                                                .addChoices(choice.getUserChoice())
                                ))
                        .setGuildOnly(true)
        ).queue();
    }

    public static void updateChoices() {
        Choice choice = new Choice();
        jda = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS).build();
        jda.retrieveCommands().queue(commands -> {
            for (Command command : commands) {
                if (command.getName().equals("addresult")) {
                    command.editCommand()
                            .addOptions(List.of(
                                    new OptionData(OptionType.STRING, "winner", "Ник победителя", true)
                                            .addChoices(choice.getUserChoice()),
                                    new OptionData(OptionType.STRING, "castlewin", "Замок победителя", true)
                                            .addChoices(choice.getCastleChoice()),
                                    new OptionData(OptionType.STRING, "loser", "Ник проигравшего", true)
                                            .addChoices(choice.getUserChoice()),
                                    new OptionData(OptionType.STRING, "castlelose", "Замок проигравшего", true)
                                            .addChoices(choice.getCastleChoice())
                            ))
                            .queue();
                    break;
                }
            }
        });
    }
}