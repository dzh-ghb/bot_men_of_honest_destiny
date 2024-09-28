package tg_bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg_bot.config.Config;

import java.util.*;

public class Bot extends TelegramLongPollingBot {
    public static final String USERNAME = Config.getUsername(); //название бота
    public static final String TOKEN = Config.getToken(); //токен

    private int msgCounter = 0; //счётчик сообщений для каждого юзера
    private final Map<Long, Integer> userData; //коллекция для параллельной работы бота с несколькими юзерами

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

    public void sendText(Long who, String txt) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .text(txt)
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendKeyboard(Long who, String txt, InlineKeyboardMarkup keyboard) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .parseMode("HTML").text(txt)
                .replyMarkup(keyboard)
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public InlineKeyboardMarkup inlineKeyboard() {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(InlineKeyboardButton.builder().text("Меня все спрашивают...").callbackData("ASK").build());
        buttons.add(InlineKeyboardButton.builder().text("Казино - рэспэкт или...").callbackData("CASINO").build());
        buttons.add(InlineKeyboardButton.builder().text("Старичок больше не пьёт?").callbackData("ALCO").build());
        buttons.add(InlineKeyboardButton.builder().text("Кто ты сегодня? - прогретый скуф или базированный МЧС?").callbackData("WHORU").build());
        buttons.add(InlineKeyboardButton.builder().text("Канал Ильюши").callbackData("MADCHANNEL").url("https://t.me/maddysontg").build());

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(buttons.get(0)))
                .keyboardRow(List.of(buttons.get(1)))
                .keyboardRow(List.of(buttons.get(2)))
                .keyboardRow(List.of(buttons.get(3)))
                .keyboardRow(List.of(buttons.get(4)))
                .build();
    }

    //код, срабатывающий каждый раз, когда происходит действие
    /*2024_09_25_проблема с CALLBACK'ми после нажатия на кнопки заключалась в том, что первые 4 переменные после if в onUpdateReceive
     *были вынесены в начало метода вне условия - постоянно шла попытка получения сообщения - сообщения не было - реакции не было,
     *после изменения реагирование на CALLBACK'и с кнопок появилось*/
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) { //обработка текстовых сообщений
            //!!!Не считывались CALLBACK'и из-за того, что три переменные ниже были указаны вне условия
            Message msg = update.getMessage(); //объект сообщения
            String msgText = msg.getText(); //текст сообщения
            long userId = msg.getFrom().getId(); //идентификатор юзера

            if (msgText.equals("/start")) { //выполняется только при первом сообщении - "/start"
                sendText(userId, "Прогрев гоев начинается через:\n3\n2\n1\nПогнали!"); //отправка приветствия
                sendText(userId, "Ну что, малютка, как я могу к тебе обращаться?");
                userData.put(userId, msgCounter);
            } else if (isStopWord(msgText)) { //завершение по стоп-слову
                if (userData.get(userId) == 0) {
                    sendText(userId, "Хреновое имя, давай-ка по новой.");
                } else {
                    sendText(userId, "ББ\n(Захочешь начать заново напиши что-нибудь)");
                    userData.put(userId, 0);
                }
            } else { //выполняется во всех остальных случаях
                if (userData.get(userId) == 0) { //текстовое сообщение после ввода имени, кроме стоп-слов
                    sendKeyboard(userId, getAnswer(msgText), inlineKeyboard());
                    userData.put(userId, ++msgCounter);
                } else {
                    sendKeyboard(userId, getAnswer(msgText), inlineKeyboard());
                    userData.put(userId, ++msgCounter);
                }
            }
        } else if (update.hasCallbackQuery()) { //обработка CALLBACK'ов с кнопок
            //обращение идет к getCallbackQuery(), а не getMessage() - так как никакого сообщения при нажатии на кнопки нет
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            sendKeyboard(chatId, getAnswer(callbackData), inlineKeyboard());
        }
    }

    private String getAnswer(String callback) {
        List<String> answers = new ArrayList<>();
        switch (callback.toUpperCase()) {
            case "ASK" -> {
                return "Друзья, где я ставлю на крупные спортивные мероприятия?\n*Контакт \"глаза в глаза\"*\nКонечно же на PARI, не забывайте юзать промокод \"ПОЖИЛОЙ СКУФ\"";
            }
            case "CASINO" -> {
                answers.add("Азартные игры - это плохо, запомните раз и навсегда, если кто-то будет Вас бэйтить на азартные игры, покер или еще какое-то дерьмо, даже если это сраный дэп, не ведитесь.");
                answers.add("У меня есть два варианта: либо я забираю эти деньги себе на карту, либо я не ухожу и заряжаю на то, что мне сказал Моденеми, но я уйду, хотя на черное я бы поставил!");
            }
            case "ALCO" -> {
                answers.add("Под ногами ящик Гиннеса");
                answers.add("Ни капли алкоголя больше, железно и точка.");
            }
            case "WHORU" -> {
                answers.add("Да ты пропердел диван, чел, ты знаешь эти четыре буквы...");
                answers.add("Про таких говорят что-то типа \"Базированный гигачад Шлёпа с квадратной челюстью\", ты славный BOY");
            }
            default -> {
                return String.format("Здорова, %s!\nСтрима, конечно же, не будет, мб пройдемся по базе?", callback);
            }
        }
        Random random = new Random();
        return answers.get(random.nextInt(answers.size()));
    }

    private boolean isStopWord(String stopWord) {
        stopWord = stopWord.toLowerCase();
        String[] stopWords = {"stop", "exit", "flugegeheimen", "стоп", "выход", "флюгегехаймен", "Нахер эту чебуречную"};
        for (String each : stopWords) {
            if (stopWord.equals(each)) {
                return true;
            }
        }
        return false;
    }
}