package tg_bot.additional;

import com.vdurmont.emoji.EmojiParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Links {
    //обработка команд со ссылками из меню
    public static List<String> getLink(String command) {
        List<String> linkWithName = new ArrayList<>();
        switch (command.toLowerCase()) {
            case "/telegram" -> {
                linkWithName.add("https://t.me/maddysontg");
                linkWithName.add(getLinkName());
            }
            case "/vk" -> {
                linkWithName.add("https://vk.com/maddysonofficial");
                linkWithName.add(getLinkName());
            }
            case "/youtube" -> {
                linkWithName.add("https://www.youtube.com/@MadHighlights");
                linkWithName.add(getLinkName());
            }
            case "/twitch" -> {
                linkWithName.add("https://www.twitch.tv/honeymad");
                linkWithName.add(getLinkName());
            }
            default -> {
                String clown = ":clown_face:";
                String linkName = EmojiParser.parseToUnicode(clown.concat(clown).concat(clown));
                linkWithName.add("https://t.me/addstickers/gifmaddyson_by_fStikBot");
                linkWithName.add(linkName);
            }
        }
        return linkWithName;
    }

    //текст, отображаемый вместо ссылок
    public static String getLinkName() {
        Random random = new Random();
        List<String> linkNames = List.of("Жмак", "Жми сюда", "Куда я жмал", "Тап", "Тык", "Тык сюда");
        return linkNames.get(random.nextInt(linkNames.size()));
    }
}
