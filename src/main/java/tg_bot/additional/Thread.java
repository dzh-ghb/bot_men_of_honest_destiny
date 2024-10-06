package tg_bot.additional;

public class Thread {
    //метод (процедура) для приостановки работы главного потока на указанное в параметрах количество миллисекунд
    public static void sleep(long millis) {
        try {
            java.lang.Thread.sleep(millis);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
