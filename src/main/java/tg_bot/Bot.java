package tg_bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg_bot.config.Config;

import java.io.File;
import java.util.*;

public class Bot extends TelegramLongPollingBot {
    public static final String USERNAME = Config.getUsername(); //название бота
    public static final String TOKEN = Config.getToken(); //токен

    private int msgCounter = 0; //счётчик сообщений для каждого юзера
    private final Map<Long, Integer> userData; //коллекция для параллельной работы бота с несколькими юзерами

    private static final String RESTART_BUTTON = "Давай по новой"; //дополнительное сообщение для рестарта бота (текст кнопки)
    private static final String STOP_BUTTON = "Нахрен эту чебуречную"; //дополнительное сообщение для завершения работы с ботом (текст кнопки)

    public Bot() {
        userData = new HashMap<>(); //создание объекта коллекции HashMap
    }

    //название бота
    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    //ключ доступа (токен) к боту
    @Override
    public String getBotToken() {
        return TOKEN;
    }

    /*------------------------------------------методы отправки сообщений------------------------------------------*/
    //отправка сообщения (параметры: кому, сообщение)
    public void sendText(Long who, String txt) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .parseMode(ParseMode.HTML).text(txt)
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    //отправка изображения
    public void sendPhotoMessage(Long who, String txt, String path) {
        SendPhoto sp = SendPhoto.builder()
                .chatId(String.valueOf(who))
                .photo(new InputFile(new File(path)))
                .parseMode(ParseMode.HTML).caption(txt)
                .build();
        try {
            execute(sp);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    //отправка сообщения с прикрепленной к СООБЩЕНИЮ клавиатурой для ответа
    public void sendKeyboard(Long who, String txt, InlineKeyboardMarkup inlineKeyboard) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .replyMarkup(inlineKeyboard)
                .parseMode(ParseMode.HTML).text(txt)
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    //отправка сообщения с прикрепленной клавиатурой, которая заменяет основную
    public void sendKeyboard(Long who, String txt, ReplyKeyboardMarkup replyKeyboard) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .replyMarkup(replyKeyboard)
                .parseMode(ParseMode.HTML).text(txt)
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    //отправка сообщения с прикрепленным изображением и встроенной клавиатурой
    public void sendKeyboard(Long who, String txt, String path, InlineKeyboardMarkup inlineKeyboard) {
        SendPhoto sp = SendPhoto.builder()
                .chatId(String.valueOf(who))
                .photo(new InputFile(new File(path)))
                .replyMarkup(inlineKeyboard)
                .parseMode(ParseMode.HTML).caption(txt)
                .build();
        try {
            execute(sp);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    //Настройка многоуровневого меню встроенной в сообщение клавиатуры
    private void buttonTap(Long id, String callbackQueryId, String callbackData, int msgId) {
        //Для изменения текста сообщения
        EditMessageText newTxt = EditMessageText.builder()
                .chatId(id.toString())
                .messageId(msgId).text("").build();
        //Для перехода к другой клавиатуре
        EditMessageReplyMarkup newKb = EditMessageReplyMarkup.builder()
                .chatId(id.toString()).messageId(msgId).build();
        //Реагирование на значение CALLBACK'а
        if (callbackData.equals("GAME")) {
            newTxt.setText("Жми уже");
            newKb.setReplyMarkup(inlineKeyboardGame());
        } else if (callbackData.equals("BACK")) {
            newTxt.setText("Стрима снова не будет.\nhttps://www.youtube.com/watch?v=FrnEnKyiWgw");
            newKb.setReplyMarkup(inlineKeyboardFirstMenu());
        }
        //завершение запроса
        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQueryId).build();
        try {
            execute(close);
            execute(newTxt);
            execute(newKb);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /*------------------------------------------методы создания и настроек клавиатур------------------------------------------*/
    //настройка встроенной в сообщение клавиатуры (первое меню)
    public InlineKeyboardMarkup inlineKeyboardFirstMenu() {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(InlineKeyboardButton.builder().text("Узнать у старичка кое-что").callbackData("GAME").build());
        buttons.add(InlineKeyboardButton.builder().text("Перейти на канал Ильюши").callbackData("MADCHANNEL").url("https://t.me/maddysontg").build());

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(buttons.get(0)))
                .keyboardRow(List.of(buttons.get(1)))
                .build();
    }

    //настройка встроенной в сообщение клавиатуры (кнопки для игры)
    public InlineKeyboardMarkup inlineKeyboardGame() {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(InlineKeyboardButton.builder().text("Немного продажничей").callbackData("ADVERTISEMENT").build());
        buttons.add(InlineKeyboardButton.builder().text("Казино - рэспэкт или...").callbackData("CASINO").build());
        buttons.add(InlineKeyboardButton.builder().text("Старичок больше не пьёт?").callbackData("ALCO").build());
        buttons.add(InlineKeyboardButton.builder().text("Кто ты сегодня? - прогретый скуф или базированный МЧС?").callbackData("WHORU").build());
        buttons.add(InlineKeyboardButton.builder().text("Сербы братишки?").callbackData("SERBS").build());
        buttons.add(InlineKeyboardButton.builder().text("Узнать вековую мудрость").callbackData("WISDOM").build());
        buttons.add(InlineKeyboardButton.builder().text("Назад").callbackData("BACK").build());

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(buttons.get(0)))
                .keyboardRow(List.of(buttons.get(1)))
                .keyboardRow(List.of(buttons.get(2)))
                .keyboardRow(List.of(buttons.get(3)))
                .keyboardRow(List.of(buttons.get(4)))
                .keyboardRow(List.of(buttons.get(5)))
                .keyboardRow(List.of(buttons.get(6)))
                .build();
    }

    //настройка клавиатуры для ответа (другой способ создания)
    public ReplyKeyboardMarkup replyKeyboard() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(RESTART_BUTTON);
        firstRow.add(STOP_BUTTON);
        keyboardRows.add(firstRow);

        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add("Канал Ильюши");
        keyboardRows.add(secondRow);

        keyboard.setKeyboard(keyboardRows);
        keyboard.setResizeKeyboard(true); //для фиксирования размера кнопок
        return keyboard;
    }

    /*------------------------------------------метод, реагирующий на любые действия в боте------------------------------------------*/
    int counter = 1, counterMSG = 1, counterCLB = 1;

    //код, срабатывающий каждый раз, когда происходит действие
    /*2024_09_25_проблема с CALLBACK'ми после нажатия на кнопки заключалась в том, что первые 4 переменные после if в onUpdateReceive
     *были вынесены в начало метода вне условия - постоянно шла попытка получения сообщения - сообщения не было - реакции не было,
     *после изменения реагирование на CALLBACK'и с кнопок появилось*/
    @Override
    public void onUpdateReceived(Update update) {
        //System.out.println("TEST - " + counter++);
        if (update.hasMessage() && update.getMessage().hasText()) { //обработка текстовых сообщений
            //!!!Не считывались CALLBACK'и из-за того, что переменные ниже были указаны вне условия
            Message msg = update.getMessage(); //объект сообщения
            String msgText = msg.getText(); //текст сообщения
            long userId = msg.getFrom().getId(); //идентификатор юзера
            System.out.println("MSG_TEST - " + counterMSG++ + " - user: " + update.getMessage().getFrom().getUserName());
            if (isStartWord(msgText)) { //выполняется при запуске/перезапуска бота
                sendPhotoMessage(userId, "Прогрев гоев начинается через:", "images/mad.jpg"); //отправка приветствия
                threadSleep(500);
                sendText(userId, "3");
                threadSleep(750); //остановка работы основного потока на 0.5 секунд
                sendText(userId, "2");
                threadSleep(750);
                sendText(userId, "1");
                threadSleep(750);
                sendText(userId, "Погнали!");
                threadSleep(600);
                sendText(userId, "Ну что, малютка, как я могу к тебе обращаться?");
                userData.put(userId, 0); //добавление информации о юзере в коллекцию
            } else if (isStopWord(msgText)) { //завершение работы с ботом по стоп-слову
                if (userData.get(userId) == 0) {
                    sendText(userId, "Хреновое имя, давай-ка еще раз.");
                } else {
                    sendText(userId, "ББ\n(Захочешь начать заново жми \"/start\")");
                    userData.put(userId, 0);
                }
            } else { //выполняется во всех остальных случаях
                if (userData.get(userId) == 0) { //текстовое сообщение после ввода имени, кроме стоп-слов
                    sendKeyboard(userId, getAnswer(msgText), inlineKeyboardFirstMenu());
                    userData.put(userId, ++msgCounter);
                } else if (isLink(msgText) || msgText.equals("Канал Ильюши")) { //если поступил запрос для получения ссылки на канал
                    sendText(userId, getLink(msgText));
                    userData.put(userId, ++msgCounter);
                } else { //текстовое сообщение, кроме стоп-слов
                    sendKeyboard(userId, "Че ты несешь??? Давай нормально или бан.\nhttps://www.youtube.com/watch?v=4rdTs9yGYbU", replyKeyboard());
                    userData.put(userId, ++msgCounter);
                }
            }
        } else if (update.hasCallbackQuery()) { //обработка CALLBACK'ов с кнопок встроенной клавиатуры
            //обращение идет к getCallbackQuery(), а не getMessage() - так как никакого сообщения при нажатии на встроенные кнопки нет
            String callbackData = update.getCallbackQuery().getData(); //возвращаемый CALLBACK
            String callbackId = update.getCallbackQuery().getId(); //идентификатор CALLBACK'а
            System.out.println("CLB_TEST - " + counterCLB++ + " - callback: " + callbackData);
            long chatId = update.getCallbackQuery().getMessage().getChatId(); //идентификатор чата
            int msgId = update.getCallbackQuery().getMessage().getMessageId(); //идентификатор сообщения
            switch (callbackData) {
                case "GAME", "BACK" -> buttonTap(chatId, callbackId, callbackData, msgId);
                case "ADVERTISEMENT" ->
                        sendKeyboard(chatId, getAnswer(callbackData), getImagePath(), inlineKeyboardGame());
                default -> sendKeyboard(chatId, getAnswer(callbackData), inlineKeyboardGame());
            }
        }
    }

    /*------------------------------------------дополнительные методы------------------------------------------*/
    //логика ответов на нажатие кнопок со встроенной клавиатуры
    private String getAnswer(String callback) {
        List<String> answers = new ArrayList<>();
        switch (callback.toUpperCase()) {
            case "ADVERTISEMENT" -> {
                answers.add("Друзья, где я ставлю на крупные спортивные мероприятия?\n*Контакт \"глаза в глаза\"*\nКонечно же на PARI, не забывайте юзать промокод \"ПОЖИЛОЙ СКУФ\"");
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
            default -> {
                return String.format("Здорова, %s!\nСтрима, конечно же, не будет, мб пройдемся по базе?\nhttps://www.youtube.com/watch?v=m07qnqQEvyQ", callback);
            }
        }
        Random random = new Random();
        return answers.get(random.nextInt(answers.size()));
    }

    //обработка команд со ссылками из меню
    private static String getLink(String command) {
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

    //получение рандомного изображения из нескольких вариантов
    private String getImagePath() {
        List<String> imagePath = List.of("images/kk.jpg", "images/pr.jpg");
        Random random = new Random();
        return imagePath.get(random.nextInt(imagePath.size()));
    }

    //проверка на запрос ссылок
    private boolean isLink(String link) {
        link = link.toLowerCase();
        String[] links = {"/telegram", "/vk", "/youtube", "/twitch"};
        for (String each : links) {
            if (link.equals(each)) {
                return true;
            }
        }
        return false;
    }

    //проверка на слово для запуска/перезапуска работы бота
    private boolean isStartWord(String startWord) {
        startWord = startWord.toLowerCase();
        String[] stopWords = {"/start", "/restart", RESTART_BUTTON.toLowerCase()};
        for (String each : stopWords) {
            if (startWord.equals(each)) {
                return true;
            }
        }
        return false;
    }

    //проверка на слово для завершения работы бота
    private boolean isStopWord(String stopWord) {
        stopWord = stopWord.toLowerCase();
        String[] stopWords = {"/stop", "stop", "exit", "flugegeheimen", "стоп", "выход", "флюгегехаймен", "Нахер эту чебуречную", STOP_BUTTON.toLowerCase()};
        for (String each : stopWords) {
            if (stopWord.equals(each)) {
                return true;
            }
        }
        return false;
    }

    //метод (процедура) для приостановки работы главного потока на указанное в параметрах количество миллисекунд
    private static void threadSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

//Аналог firstInlineKeyboard и sendMenu(who, text, keyboard)
//            public static SendMessage inlineKeyboard(long chatId) {
//                    SendMessage message = new SendMessage();
//                    message.setChatId(String.valueOf(chatId));
//                    message.setText("TEST");
//
//                    List<InlineKeyboardButton> buttons = new ArrayList<>();
//                    buttons.add(InlineKeyboardButton.builder().text("Меня все спрашивают...").callbackData("ADVERTISEMENT").build());
//                    buttons.add(InlineKeyboardButton.builder().text("Казино - рэспэкт или...").callbackData("CASINO").build());
//                    buttons.add(InlineKeyboardButton.builder().text("Старичок больше не пьёт?").callbackData("ALCO").build());
//                    buttons.add(InlineKeyboardButton.builder().text("Кто ты сегодня? - прогретый скуф или базированный МЧС?").callbackData("WHORU").build());
//                    buttons.add(InlineKeyboardButton.builder().text("Канал Ильюши").callbackData("MADCHANNEL").url("https://t.me/maddysontg").build());
//
//                    InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
//                            .keyboardRow(List.of(buttons.get(0)))
//                            .keyboardRow(List.of(buttons.get(1)))
//                            .keyboardRow(List.of(buttons.get(2)))
//                            .keyboardRow(List.of(buttons.get(3)))
//                            .keyboardRow(List.of(buttons.get(4)))
//                            .build();
//
//                    message.setReplyMarkup(keyboard);
//
//                    return message;
//                }
//                try {
//                    execute(inlineKeyboard(chatId));
//                } catch (TelegramApiException e) {
//                    System.out.println("EXCEPTION");
//                }
//Перезапись текста сообщения (ТОЛЬКО СООБЩЕНИЯ БОТА) после нажатия кнопки, прикрепленной к нему
//            if (callbackData.equals("ADVERTISEMENT")) {
//                String text = "ADVERTISEMENT";
//                EditMessageText message = new EditMessageText(); //класс, позволяющий изменять сообщения (в т.ч. уже отосланные)
//                message.setChatId(String.valueOf(chatId)); //ID чата, где будет изменяться сообщение
//                message.setText(text); //устанавливаем текст для замены
//                message.setMessageId((int) messageId); //установка ID сообщения, текст которого будет изменяться
//                try {
//                    execute(message); //
//                } catch (TelegramApiException e) {
//                    System.out.println(e.getMessage());
//                }
//            }