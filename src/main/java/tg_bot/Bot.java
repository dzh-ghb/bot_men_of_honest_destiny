package tg_bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
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
import tg_bot.additional.Thread;
import tg_bot.additional.*;
import tg_bot.config.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bot extends TelegramLongPollingBot {
    private static final String USERNAME = Config.getUsername(); //название бота
    private static final String TOKEN = Config.getToken(); //токен

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
    public void sendText(Long chatId, String txt) {
        SendMessage sm = SendMessage.builder()
                .chatId(chatId.toString())
                .parseMode(ParseMode.HTML).text(txt)
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    //отправка ответа на конкретное сообщение
    public void sendText(Long chatId, Integer replyToMsgId, String txt) {
        SendMessage sm = SendMessage.builder()
                .chatId(chatId.toString())
                .replyToMessageId(replyToMsgId)
                .parseMode(ParseMode.HTML).text(txt)
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    //отправка изображения
    public void sendPhotoMessage(Long chatId, String txt, String path) {
        SendPhoto sp = SendPhoto.builder()
                .chatId(String.valueOf(chatId))
                .photo(new InputFile(new File(path)))
                .parseMode(ParseMode.HTML).caption(txt)
                .build();
        try {
            execute(sp);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    //отправка сообщения с прикрепленной к СООБЩЕНИЮ клавиатурой для ответа
    public void sendKeyboard(Long chatId, String txt, InlineKeyboardMarkup inlineKeyboard) {
        SendMessage sm = SendMessage.builder()
                .chatId(chatId.toString())
                .replyMarkup(inlineKeyboard)
                .parseMode(ParseMode.HTML).text(txt)
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    //отправка сообщения с прикрепленной клавиатурой, которая заменяет основную (в ответ на конкретное сообщение)
    public void sendKeyboard(Long chatId, Integer replyToMsgId, String txt, ReplyKeyboardMarkup replyKeyboard) {
        SendMessage sm = SendMessage.builder()
                .chatId(chatId.toString())
                .replyToMessageId(replyToMsgId)
                .replyMarkup(replyKeyboard)
                .parseMode(ParseMode.HTML).text(txt)
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    //отправка сообщения с прикрепленным изображением и встроенной клавиатурой
    public void sendKeyboard(Long chatId, String caption, String pathname, InlineKeyboardMarkup inlineKeyboard) {
        SendPhoto sp = SendPhoto.builder()
                .chatId(String.valueOf(chatId))
                .photo(new InputFile(new File(pathname)))
                .replyMarkup(inlineKeyboard)
                .parseMode(ParseMode.HTML).caption(caption)
                .build();
        try {
            execute(sp);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    //настройка многоуровневого меню встроенной в сообщение клавиатуры
    private void buttonTap(Long chatId, String callbackQueryId, String callbackData, int msgId) {
        //Для изменения текста сообщения
        EditMessageText newTxt = EditMessageText.builder()
                .chatId(chatId.toString())
                .messageId(msgId).text("").build();
        //Для перехода к другой клавиатуре
        EditMessageReplyMarkup newKb = EditMessageReplyMarkup.builder()
                .chatId(chatId.toString()).messageId(msgId).build();
        //Реагирование на значение CALLBACK'а
        switch (callbackData) {
            case "GAME" -> {
                newTxt.setText("Нажми кнопку и узнай, что скажет король");
                newKb.setReplyMarkup(inlineKeyboardGame());
            }
            case "BACK" -> {
                newTxt.setText("Стрима снова не будет.\nhttps://www.youtube.com/watch?v=FrnEnKyiWgw");
                newKb.setReplyMarkup(inlineKeyboardFirstMenu());
            }
            case "ADVERTISEMENT" -> {
                deleteMessage(chatId, msgId);
                sendKeyboard(chatId, Answers.getAnswer(callbackData), ImagePaths.getImagePath(), inlineKeyboardAfterChoice()); /*отправка изображения с текстом в описании,
                    замена текста и клавиатуры не происходит, так как здесь идет работа не с SendMessage (текстовым сообщением), а с SendPhoto (изображением с описанием),
                    решение: удаление текста предыдущего сообщения (меню категорий) перед выводом ответа*/
            }
            default -> {
                newTxt.setText(Answers.getAnswer(callbackData));
                newKb.setReplyMarkup(inlineKeyboardAfterChoice());
            }
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

    //удаление сообщения
    public void deleteMessage(Long chatId, int msgId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId.toString());
        deleteMessage.setMessageId(msgId);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
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

    //настройка встроенной в сообщение клавиатуры (кнопка для возврата к основному меню)
    public InlineKeyboardMarkup inlineKeyboardAfterChoice() {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(InlineKeyboardButton.builder().text("Вернуться назад").callbackData("BACKTOGAME").build());

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(buttons.get(0)))
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
            int msgId = msg.getMessageId(); //идентификатор сообщения
            long userId = msg.getFrom().getId(); //идентификатор юзера
            System.out.println("MSG_TEST - " + counterMSG++ + " - user: " + update.getMessage().getFrom().getUserName());
            if (KeyWords.isStartWord(msgText, RESTART_BUTTON)) { //выполняется при запуске/перезапуска бота
                sendPhotoMessage(userId, "Прогрев гоев начинается через:", "images/mad.jpg"); //отправка приветствия
                Thread.sleep(500); //остановка работы основного потока на 0.5 секунд
                sendText(userId, "3");
                Thread.sleep(750);
                sendText(userId, "2");
                Thread.sleep(750);
                sendText(userId, "1");
                Thread.sleep(750);
                sendText(userId, "Погнали!");
                Thread.sleep(600);
                sendText(userId, "Ну что, малютка, как я могу к тебе обращаться?");
                userData.put(userId, 0); //добавление информации о юзере в коллекцию
            } else if (KeyWords.isStopWord(msgText, STOP_BUTTON)) { //завершение работы с ботом по стоп-слову
                if (userData.get(userId) == 0) {
                    sendText(userId, msgId, "Хреновое имя, давай-ка еще раз.");
                } else {
                    sendText(userId, "ББ\n(Захочешь начать заново жми \"/start\")");
                    userData.put(userId, 0);
                }
            } else if (KeyWords.isLink(msgText) && userData.get(userId) == 0) { //если кнопка со ссылкой на канал из меню нажата на этапе ввода имени
                sendText(userId, msgId, "Воу-воу, не спеши, сначала представься)");
            } else { //выполняется во всех остальных случаях
                if (userData.get(userId) == 0) { //текстовое сообщение после ввода имени, кроме стоп-слов
                    sendKeyboard(userId, Answers.getAnswer(msgText), inlineKeyboardFirstMenu());
                    userData.put(userId, ++msgCounter);
                } else if (KeyWords.isLink(msgText) || msgText.equals("Канал Ильюши")) { //если поступил запрос для получения ссылки на канал
                    sendText(userId, Links.getLink(msgText));
                    userData.put(userId, ++msgCounter);
                } else { //текстовое сообщение, кроме стоп-слов
                    sendKeyboard(userId, msgId, "Че ты несешь??? Давай нормально или бан.\nhttps://www.youtube.com/watch?v=4rdTs9yGYbU", replyKeyboard());
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
            if (callbackData.equals("BACKTOGAME")) {
                sendKeyboard(chatId, Answers.getAnswer(callbackData), inlineKeyboardGame());
            } else {
                buttonTap(chatId, callbackId, callbackData, msgId);
            }
        }
    }
}