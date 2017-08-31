package edu.uic.people.yxing7;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class Search {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			String p = "harvey";
			double latitude = 29.7604;
			double longitude = -95.3698;//Coordinates of Houston
			int radius = 500;//In miles
			ConfigurationBuilder cb = new ConfigurationBuilder();
			boolean searchForward = false;
			FileWriter fw = null;
			cb.setDebugEnabled(true)
				.setOAuthConsumerKey("KR1oOcNiIiOs5nQw9XPKWSHcr")
				.setOAuthConsumerSecret("1hc9K7q3h13Hvv2JaDdCWAUyTvBOnryiXOCpaPqC4RR5Ihm5No")
				.setOAuthAccessToken("902703427725271041-xvkHRAvN2M3dyIrQNUFVyp77u04t7f8")
				.setOAuthAccessTokenSecret("fRvWhpuBrdJAOz5t3Fj2uwf2WhyahTBwefge1Lvdp0qT2");//Change for every account
			TwitterFactory tf = new TwitterFactory(cb.build());
			Twitter t = tf.getInstance();
			try {
				fw = new FileWriter("tweets.txt");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(searchForward){
				while(true){
					try {
			            Query query = new Query();
			            query.setGeoCode(new GeoLocation(latitude, longitude), radius, Query.Unit.mi);
			            QueryResult result;
			            do {
			                result = t.search(query);
			                List<Status> tweets = result.getTweets();
			                for (Status tweet : tweets) {
			                    //System.out.println(tweet.getId() + " - " + tweet.getCreatedAt());
			                    try {
									fw.write(tweet.getText()+"\n");
									fw.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
			                }
			            } while ((query = result.nextQuery()) != null);
			        } catch (TwitterException te) {
			            te.printStackTrace();
			            System.out.println("Failed to search tweets: " + te.getMessage());
			            System.exit(-1);
			        }
					try {
						Thread.sleep(20000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else{
				long maxId = 0;
				while(true){
					try {
			            Query query = new Query();
			            query.setGeoCode(new GeoLocation(latitude, longitude), radius, Query.Unit.mi);
			            QueryResult result;
			            if(maxId!=0){
			            	query.setMaxId(maxId);
			            }
			            do {
			                result = t.search(query);
			                List<Status> tweets = result.getTweets();
			                for (Status tweet : tweets) {
			                    //System.out.println(n+ tweet.getId() + " - " + tweet.getCreatedAt());
			                	try {
			                		System.out.println(tweet.getText());
									fw.write(tweet.getText()+"\n"+"\n");
									fw.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
			                    maxId = tweet.getId()-1;//do not duplicate
			                }
			            } while ((query = result.nextQuery()) != null);
			        } catch (TwitterException te) {
			            te.printStackTrace();
			            System.out.println("Failed to search tweets: " + te.getMessage());
			            System.exit(-1);
			        }
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
	}

}
