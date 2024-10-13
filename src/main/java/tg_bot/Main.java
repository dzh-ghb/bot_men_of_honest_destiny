package tg_bot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import tg_bot.config.Config;

public class Main {
    public static void main(String[] args) {
        Config.readData(); //считывание данных об имени и токене бота из файлов
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            Bot bot = new Bot();
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }
}