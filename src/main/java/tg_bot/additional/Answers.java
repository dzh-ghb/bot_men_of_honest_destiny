package tg_bot.additional;

import java.util.*;

public class Answers {
    //логика ответов на нажатие кнопок со встроенной клавиатуры
    public static List<String> getAnswer(String callback) {
        List<String> answers = new ArrayList<>(); //для хранения текстовки
        Map<String, String> answersAndLinks = new LinkedHashMap<>(); //для хранения текстовки и ссылок на видео в формате ключ-значение (в порядке добавления)
        List<String> answerList = new ArrayList<>(); //для формирования возвращаемого результата
        switch (callback.toUpperCase()) {
            case "ADVERTISEMENT" -> {
                answers.add("Друзья, где я ставлю на крупные спортивные мероприятия?\n*Контакт \"глаза в глаза\"*\nКонечно же на PARI, не забывайте юзать промокод \"ПОЖИЛОЙ СКУФ\"");
                answers.add("Друзья, ну постоянно меня спрашиваете, где же я собираюсь ставить, на что я собираюсь ставить, но пора бы уже запомнить, что ставлю я на сайте PARI, либо в их прекрасном мобильном приложении...");
                answers.add("В Steam ежемесячно выходят новые игры, а мы продолжаем сталкиваться с текстом о том, что данный товар недоступен в нашем регионе\n" +
                        "ХВАТИТ ЭТО ТЕРПЕТЬ!\nСкорее на КупиКод, сайт полностью, даже вместе со скидками, повторяет библиотеку Steam, " +
                        "а в разделе \"Недоступные в РФ\" находятся игры, которые в самом магазине Steam пользователи из РФ найти не смогут");
            }
            case "CASINO" -> {
                answers.add("Азартные игры - это плохо, запомните раз и навсегда, если кто-то будет Вас бэйтить на азартные игры, покер или еще какое-то дерьмо, даже если это сраный дэп, не ведитесь.\n");
                answersAndLinks.put(answers.get(0), "https://www.youtube.com/watch?v=wcFQFZhf0gw");
                answers.add("У меня есть два варианта: либо я забираю эти деньги себе на карту, либо я не ухожу и заряжаю на то, что мне сказал Моденеми, но я уйду, хотя на черное я бы поставил!\n");
                answersAndLinks.put(answers.get(1), "https://www.youtube.com/watch?v=v3zcWMeexTU");
            }
            case "ALCO" -> {
                answers.add("Под ногами ящик Гиннеса\n");
                answersAndLinks.put(answers.get(0), "https://www.youtube.com/watch?v=9Ji_3uCc0MA");
                answers.add("Ни капли алкоголя больше, твердо и четко.\n");
                answersAndLinks.put(answers.get(1), "https://www.youtube.com/watch?v=LEBYYTugnoQ");
            }
            case "WHORU" -> {
                answers.add("Да ты пропердел диван, чел, ты знаешь эти четыре буквы...\n");
                answersAndLinks.put(answers.get(0), "https://www.youtube.com/watch?v=KaHK8DUg8pE");
                answers.add("Про таких говорят что-то типа \"Базированный гигачад Шлёпа с квадратной челюстью\", ты славный BOY\n");
                answersAndLinks.put(answers.get(1), "https://www.youtube.com/watch?v=BXVFKvYW3x0");
            }
            case "SERBS" -> {
                answers.add("Да-браћо,\nсада више волим ракију, вотка није укусна, ракија је укусна, воће, знате, врло укусно\n");
                answersAndLinks.put(answers.get(0), "https://www.youtube.com/watch?v=xokptzlDUU8");
                answers.add("...ненавижу сербов, ракию, бухло, культуру, я их даже братишками не считаю\n*Новость: \"Мэддисон покинул Сербию...\"*\n");
                answersAndLinks.put(answers.get(1), "https://www.youtube.com/watch?v=kTujYsiHiwA");
            }
            case "WISDOM" -> {
                answers.add("Мальчик - посмеется.\nМужчина - скажет спасибо...\n");
                answersAndLinks.put(answers.get(0), "https://www.youtube.com/watch?v=KWxfn-3-IUM");
            }
            case "BACKTOGAME" -> {
                answers.add("Малютка, жмай на кнопку");
                answers.add("Окей, давай еще разок");
                answers.add("Не борзей, старичок");
            }
            default -> {
                answers.add(String.format("Здорова, %s!\nСтрима, конечно же, не будет, мб пройдемся по базе?\n", callback));
                answersAndLinks.put(answers.get(0), "https://www.youtube.com/watch?v=m07qnqQEvyQ");
            }
        }
        Random random = new Random();
        String randomAnswer = answers.get(random.nextInt(answers.size()));
        answerList.add(randomAnswer);
        answerList.add(answersAndLinks.get(randomAnswer));
        return answerList;
    }
}
