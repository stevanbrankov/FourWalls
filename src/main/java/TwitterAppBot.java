import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterAppBot {

    private static String twitterConsumerKey = System.getenv("TWITTER_CONSUMER_KEY");
    private static String twitterConsumerSecret = System.getenv("TWITTER_CONSUMER_SECRET");
    private static String twitterAccessToken = System.getenv("TWITTER_ACCESS_TOKEN");
    private static String twitterAccessTokenSecret = System.getenv("TWITTER_ACCESS_TOKEN_SECRET");

    private TwitterAppBot() {
    }

    public static Twitter init() {
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(twitterConsumerKey, twitterConsumerSecret);
        AccessToken accessToken = new AccessToken(twitterAccessToken, twitterAccessTokenSecret);
        twitter.setOAuthAccessToken(accessToken);
        return twitter;
    }


}
