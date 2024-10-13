package tg_bot.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Config {
    private static String USERNAME; //название бота
    private static String TOKEN; //токен бота

    public static String getUsername() {
        return USERNAME;
    }

    public static String getToken() {
        return TOKEN;
    }

    public static void readData() {
        File dir = new File("config/");
        File username = new File(dir, "username.txt");
        File token = new File(dir, "token.txt");
        try {
            dir.mkdirs();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        USERNAME = inputStream(username);
        TOKEN = inputStream(token);
    }

    private static String inputStream(File fileToRead) {
        try (InputStream inputStream = new FileInputStream(fileToRead)) {
            StringBuilder result = new StringBuilder();
            byte[] array = new byte[64]; //массив на 64 элемента
            int count = inputStream.read(array); //количество считанных элементов
            while (count > 0) { //считывать пока прочитано один и более элементов
                result.append(new String(array), 0, count); //создание строки из массива байт
                count = inputStream.read(array); //считывание следующего массива байт
            }
            return result.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
}