package tg_bot.additional;

import java.util.*;

public class Responses {
    //логика ответов на нажатие кнопок со встроенной клавиатуры
    public static List<String> getResponse(String callback) {
        List<String> responses = new ArrayList<>(); //для хранения текстовки
        Map<String, String> responsesAndLinks = new LinkedHashMap<>(); //для хранения текстовки и ссылок на видео в формате ключ-значение (в порядке добавления)
        List<String> responseList = new ArrayList<>(); //для формирования возвращаемого результата
        switch (callback.toUpperCase()) {
            case "ADVERTISEMENT" -> responses.add(Paths.getAudioPath());
            case "CASINO" -> {
                responses.add("Азартные игры - это плохо, запомните раз и навсегда, если кто-то будет Вас бэйтить на азартные игры, покер или еще какое-то дерьмо, даже если это сраный дэп, не ведитесь.\n");
                responsesAndLinks.put(responses.get(0), "https://www.youtube.com/watch?v=wcFQFZhf0gw");
                responses.add("У меня есть два варианта: либо я забираю эти деньги себе на карту, либо я не ухожу и заряжаю на то, что мне сказал Моденеми, но я уйду, хотя на черное я бы поставил!\n");
                responsesAndLinks.put(responses.get(1), "https://www.youtube.com/watch?v=v3zcWMeexTU");
            }
            case "ALCO" -> {
                responses.add("Под ногами ящик Гиннеса\n");
                responsesAndLinks.put(responses.get(0), "https://www.youtube.com/watch?v=9Ji_3uCc0MA");
                responses.add("Ни капли алкоголя больше, твердо и четко.\n");
                responsesAndLinks.put(responses.get(1), "https://www.youtube.com/watch?v=LEBYYTugnoQ");
            }
            case "WHORU" -> {
                responses.add("Да ты пропердел диван, чел, ты знаешь эти четыре буквы...\n");
                responsesAndLinks.put(responses.get(0), "https://www.youtube.com/watch?v=KaHK8DUg8pE");
                responses.add("Про таких говорят что-то типа \"Базированный гигачад Шлёпа с квадратной челюстью\", ты славный BOY\n");
                responsesAndLinks.put(responses.get(1), "https://www.youtube.com/watch?v=BXVFKvYW3x0");
            }
            case "SERBS" -> {
                responses.add("Да-браћо,\nсада више волим ракију, вотка није укусна, ракија је укусна, воће, знате, врло укусно\n");
                responsesAndLinks.put(responses.get(0), "https://www.youtube.com/watch?v=xokptzlDUU8");
                responses.add("...ненавижу сербов, ракию, бухло, культуру, я их даже братишками не считаю\n*Новость: \"Мэддисон покинул Сербию...\"*\n");
                responsesAndLinks.put(responses.get(1), "https://www.youtube.com/watch?v=kTujYsiHiwA");
            }
            case "WISDOM" -> {
                responses.add("Мальчик - посмеется.\nМужчина - скажет спасибо...\n");
                responsesAndLinks.put(responses.get(0), "https://www.youtube.com/watch?v=KWxfn-3-IUM");
            }
            case "BACKTOGAME" -> {
                responses.add("Малютка, жмай на кнопку");
                responses.add("Окей, давай еще разок");
                responses.add("Не борзей, старичок");
            }
            default -> {
                responses.add(String.format("Здорова, %s!\nСтрима, конечно же, не будет, мб пройдемся по базе?\n", callback));
                responsesAndLinks.put(responses.get(0), "https://www.youtube.com/watch?v=m07qnqQEvyQ");
            }
        }
        Random random = new Random();
        String randomResponse = responses.get(random.nextInt(responses.size()));
        responseList.add(randomResponse);
        responseList.add(responsesAndLinks.get(randomResponse));
        return responseList;
    }
}
