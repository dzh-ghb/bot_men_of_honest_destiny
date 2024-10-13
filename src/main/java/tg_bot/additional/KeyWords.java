package tg_bot.additional;

public class KeyWords {
    //проверка на запрос ссылок
    public static boolean isLink(String link) {
        link = link.toLowerCase();
        String[] links = {"/stickers", "/telegram", "/vk", "/youtube", "/twitch"};
        for (String each : links) {
            if (link.equals(each)) {
                return true;
            }
        }
        return false;
    }

    //проверка на слово для запуска/перезапуска работы бота
    public static boolean isStartWord(String startWord, String restartButton) {
        startWord = startWord.toLowerCase();
        String[] stopWords = {"/start", "/restart", restartButton.toLowerCase()};
        for (String each : stopWords) {
            if (startWord.equals(each)) {
                return true;
            }
        }
        return false;
    }

    //проверка на слово для завершения работы бота
    public static boolean isStopWord(String stopWord, String stopButton) {
        stopWord = stopWord.toLowerCase();
        String[] stopWords = {"/stop", "stop", "exit", "flugegeheimen", "стоп", "выход", "флюгегехаймен", "Нахер эту чебуречную", stopButton.toLowerCase()};
        for (String each : stopWords) {
            if (stopWord.equals(each)) {
                return true;
            }
        }
        return false;
    }
}
