package psiap.resiliance.harvey.tweetcrawler;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {

    public static final String TWITTER_ACCOUNT_INFO_FILE = "TwitterAccountInfo.properties";

    public static void main(String[] args) throws IOException {
        Properties p = new Properties();
        try (FileInputStream fis = new FileInputStream("TwitterAccountInfo.properties")) {
            p.load(fis);
        }
        TwitterCrawler crawler = new TwitterCrawler(p);
        try (FileTweetSaver saver = new FileTweetSaver("output.json")) {
            crawler.searchRadius(29.7604, -95.3698, 500, false, saver);
        }

    }

    public static void initProperties() throws IOException {
        Properties p = new Properties();
        p.setProperty(TwitterCrawler.AUTH_COMSUMER_KEY_PROPERTY, "KR1oOcNiIiOs5nQw9XPKWSHcr");
        p.setProperty(TwitterCrawler.AUTH_COMSUMER_SECRET_PROPERTY, "1hc9K7q3h13Hvv2JaDdCWAUyTvBOnryiXOCpaPqC4RR5Ihm5No");
        p.setProperty(TwitterCrawler.AUTH_ACCESS_TOKEN_PROPERTY, "902703427725271041-xvkHRAvN2M3dyIrQNUFVyp77u04t7f8");
        p.setProperty(TwitterCrawler.AUTH_ACCESS_TOKEN_SECRET_PROPERTY, "fRvWhpuBrdJAOz5t3Fj2uwf2WhyahTBwefge1Lvdp0qT2");
        try (FileOutputStream fos = new FileOutputStream(TWITTER_ACCOUNT_INFO_FILE)) {
            p.store(fos, "Authorization information for Twitter");
        }

    }

}
