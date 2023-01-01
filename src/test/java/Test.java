import java.io.IOException;
import java.util.Set;

public class Test {

    @org.testng.annotations.Test
    public static void API() throws IOException, InterruptedException {
        Set<String> apartments = FourWalls.getApartments();
        WhatsAppBot.init();
        for (String apartment : apartments) {
            WhatsAppBot.sendMessage(apartment);
        }
    }
}
