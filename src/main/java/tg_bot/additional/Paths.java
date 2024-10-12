package tg_bot.additional;

import java.util.List;
import java.util.Random;

public class Paths {
    //получение рандомного изображения из нескольких вариантов
    public static String getImagePath() {
        List<String> imagePath = List.of("images/pr/pr0.jpg", "images/pr/pr1.jpg", "images/pr/pr2.jpg", "images/pr/pr3.jpg");
        Random random = new Random();
        return imagePath.get(random.nextInt(imagePath.size()));
    }

    //получение рандомного аудиосообщения из нескольких вариантов
    public static String getAudioPath() {
        List<String> audioPath = List.of("audio/pr0.ogg", "audio/pr1.ogg");
        Random random = new Random();
        return audioPath.get(random.nextInt(audioPath.size()));
    }
}
