import java.io.IOException;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Set<String> apartments = FourWalls.getApartments();
        WhatsAppBot.init();
        for (String apartment : apartments) {
            WhatsAppBot.sendMessage(apartment);
        }
    }
}
