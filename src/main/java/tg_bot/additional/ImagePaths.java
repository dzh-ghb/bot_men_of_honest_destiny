package tg_bot.additional;

import java.util.List;
import java.util.Random;

public class ImagePaths {
    //получение рандомного изображения из нескольких вариантов
    public static String getImagePath() {
        List<String> imagePath = List.of("images/pr/pr0.jpg", "images/pr/pr1.jpg", "images/pr/pr2.jpg");
        Random random = new Random();
        return imagePath.get(random.nextInt(imagePath.size()));
    }
}
