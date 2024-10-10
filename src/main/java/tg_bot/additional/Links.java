package tg_bot.additional;

import java.util.List;
import java.util.Random;

public class Links {
    //обработка команд со ссылками из меню
    public static String getLink(String command) {
        switch (command.toLowerCase()) {
            case "/vk" -> {
                return "https://vk.com/maddysonofficial";
            }
            case "/youtube" -> {
                return "https://www.youtube.com/@MadHighlights";
            }
            case "/twitch" -> {
                return "https://www.twitch.tv/honeymad";
            }
            default -> {
                return "https://t.me/maddysontg";
            }
        }
    }

    //Текст, отображаемый вместо ссылки
    public static String getLinkName() {
        Random random = new Random();
        List<String> linkNames = List.of("Жмак", "Жми сюда", "Куда я жмал", "Тап", "Тык", "Тык сюда");
        return linkNames.get(random.nextInt(linkNames.size()));
    }
}
