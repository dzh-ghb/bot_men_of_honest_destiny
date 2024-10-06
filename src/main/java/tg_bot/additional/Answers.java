package tg_bot.additional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Answers {
    //логика ответов на нажатие кнопок со встроенной клавиатуры
    public static String getAnswer(String callback) {
        List<String> answers = new ArrayList<>();
        switch (callback.toUpperCase()) {
            case "ADVERTISEMENT" -> {
                answers.add("Друзья, где я ставлю на крупные спортивные мероприятия?\n*Контакт \"глаза в глаза\"*\nКонечно же на PARI, не забывайте юзать промокод \"ПОЖИЛОЙ СКУФ\"");
                answers.add("Друзья, ну постоянно меня спрашиваете, где же я собираюсь ставить, на что я собираюсь ставить, но пора бы уже запомнить, что ставлю я на сайте PARI, либо в их прекрасном мобильном приложении...");
                answers.add("В Steam ежемесячно выходят новые игры, а мы продолжаем сталкиваться с текстом о том, что данный товар недоступен в нашем регионе\n" +
                        "ХВАТИТ ЭТО ТЕРПЕТЬ!\nСкорее на КупиКод, сайт полностью, даже вместе со скидками, повторяет библиотеку Steam, " +
                        "а в разделе \"Недоступные в РФ\" находятся игры, которые в самом магазине Steam пользователи из РФ найти не смогут");
            }
            case "CASINO" -> {
                answers.add("Азартные игры - это плохо, запомните раз и навсегда, если кто-то будет Вас бэйтить на азартные игры, покер или еще какое-то дерьмо, даже если это сраный дэп, не ведитесь.\nhttps://www.youtube.com/watch?v=wcFQFZhf0gw");
                answers.add("У меня есть два варианта: либо я забираю эти деньги себе на карту, либо я не ухожу и заряжаю на то, что мне сказал Моденеми, но я уйду, хотя на черное я бы поставил!\nhttps://www.youtube.com/watch?v=v3zcWMeexTU");
            }
            case "ALCO" -> {
                answers.add("Под ногами ящик Гиннеса\nhttps://www.youtube.com/watch?v=9Ji_3uCc0MA");
                answers.add("Ни капли алкоголя больше, твердо и четко.\nhttps://www.youtube.com/watch?v=LEBYYTugnoQ");
            }
            case "WHORU" -> {
                answers.add("Да ты пропердел диван, чел, ты знаешь эти четыре буквы...\nhttps://www.youtube.com/watch?v=KaHK8DUg8pE");
                answers.add("Про таких говорят что-то типа \"Базированный гигачад Шлёпа с квадратной челюстью\", ты славный BOY\nhttps://www.youtube.com/watch?v=BXVFKvYW3x0");
            }
            case "SERBS" -> {
                answers.add("Да-браћо,\nсада више волим ракију, вотка није укусна, ракија је укусна, воће, знате, врло укусно\nhttps://www.youtube.com/watch?v=xokptzlDUU8");
                answers.add("...ненавижу сербов, ракию, бухло, культуру, я их даже братишками не считаю\n*Новость: \"Мэддисон покинул Сербию...\"*\nhttps://www.youtube.com/watch?v=kTujYsiHiwA");
            }
            case "WISDOM" ->
                    answers.add("Мальчик - посмеется.\nМужчина - скажет спасибо...\nhttps://www.youtube.com/watch?v=KWxfn-3-IUM");
            case "BACKTOGAME" -> {
                answers.add("Малютка, жмай на кнопку");
                answers.add("Окей, давай еще разок");
                answers.add("Не борзей, старичок");
            }
            default -> {
                return String.format("Здорова, %s!\nСтрима, конечно же, не будет, мб пройдемся по базе?\nhttps://www.youtube.com/watch?v=m07qnqQEvyQ", callback);
            }
        }
        Random random = new Random();
        return answers.get(random.nextInt(answers.size()));
    }
}
