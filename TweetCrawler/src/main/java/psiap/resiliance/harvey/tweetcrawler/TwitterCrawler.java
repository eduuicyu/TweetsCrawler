package psiap.resiliance.harvey.tweetcrawler;

import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Crawler for Twitter.
 */
public class TwitterCrawler {

    public static final String AUTH_COMSUMER_KEY_PROPERTY = "authConsumerKey";
    public static final String AUTH_COMSUMER_SECRET_PROPERTY = "authConsumerSecret";
    public static final String AUTH_ACCESS_TOKEN_PROPERTY = "authAccessToken";
    public static final String AUTH_ACCESS_TOKEN_SECRET_PROPERTY = "authAccessTokenSecret";

    private final ConfigurationBuilder builder;
    private final Configuration conf;
    private final TwitterFactory factory;
    private final Twitter twitter;
    private static final Logger LOG = Logger.getLogger(TwitterCrawler.class.getName());

    public TwitterCrawler(Properties p) {
        this(p.getProperty(AUTH_COMSUMER_KEY_PROPERTY), p.getProperty(AUTH_COMSUMER_SECRET_PROPERTY),
                p.getProperty(AUTH_ACCESS_TOKEN_PROPERTY), p.getProperty(AUTH_ACCESS_TOKEN_SECRET_PROPERTY));
    }

    public TwitterCrawler(String authConsumerKey, String authConsumerSecret, String authAccessToken, String authAccessTokenSecret) {
        builder = new ConfigurationBuilder();
        builder.setDebugEnabled(true)
                .setOAuthConsumerKey(authConsumerKey)
                .setOAuthConsumerSecret(authConsumerSecret)
                .setOAuthAccessToken(authAccessToken)
                .setOAuthAccessTokenSecret(authAccessTokenSecret);
        conf = builder.build();
        factory = new TwitterFactory(conf);
        twitter = factory.getInstance();
    }

    private static final int SLEEP_ON_FORWARD = 20000;
    private static final int SLEEP_ON_BACKWARD = 1000;

    /**
     * Search Twitter based on geo-location.
     *
     * @param latitude The Latitude of the center location.
     * @param longitude The Longitude of the center location.
     * @param radius The radius of the area, in miles.
     * @param searchFoward If the query is searching for new (and latest
     * Tweets).
     * @param tweetHandler The handler of the crawled tweets.
     */
    public void searchRadius(double latitude, double longitude, double radius, boolean searchFoward, Consumer<Status> tweetHandler) {
        long maxId = 0;
        long delay = searchFoward ? SLEEP_ON_FORWARD : SLEEP_ON_BACKWARD;

        while (true) {
            try {
                Query query = new Query();
                query.setGeoCode(new GeoLocation(latitude, longitude), radius, Query.Unit.mi);
                QueryResult result;
                if (!searchFoward && maxId != 0) {
                    query.setMaxId(maxId);
                }
                do {
                    LOG.log(Level.INFO, "Sending query...");
                    result = twitter.search(query);
                    List<Status> tweets = result.getTweets();
                    LOG.log(Level.INFO, String.format("Got results: %d tweets.", tweets.size()));
                    for (Status tweet : tweets) {
                        tweetHandler.accept(tweet);
                        maxId = tweet.getId() - 1;//do not duplicate
                    }
                } while ((query = result.nextQuery()) != null);
            } catch (TwitterException te) {
                LOG.log(Level.SEVERE, "Failed in searching tweets", te);
                return;
            }
            try {
                LOG.log(Level.INFO, String.format("Wait for: %dms", delay));
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                LOG.log(Level.WARNING, "Failed in searching tweets", e);
            }
        }
    }

}
