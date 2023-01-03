import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.io.IOException;
import java.util.Set;

public class Test {

    @org.testng.annotations.Test
    public static void API() throws IOException, InterruptedException, TwitterException {
        Set<String> apartments = FourWalls.getApartments();
        Twitter twitter = TwitterAppBot.init();
        if (!apartments.isEmpty()) System.out.println("New:");
        for (String apartment : apartments) {
            System.out.println(apartment);
            twitter.sendDirectMessage("FourWallsBro", apartment);
            Thread.sleep(1500);
        }
    }
}
