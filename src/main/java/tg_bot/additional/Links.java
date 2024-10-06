package tg_bot.additional;

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
}
