/*
 * The MIT License (MIT)

 * Copyright (c) 2014 Michael D. Vinci

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import org.jinstagram.Instagram;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.*;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;
import org.jinstagram.entity.common.Images;
import org.jinstagram.entity.common.Location;
import org.jinstagram.entity.tags.TagMediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;

/**
 *
 * @author michaeldvinci
 */
public class GetLiveMedia {
    private String authorizationUrl, twitterString;
    private int creationTime;
    private double tXCoord, tYCoord, iXCoord, iYCoord;
    private static Query query;
    private static QueryResult result;
    private static Twitter twitter;
    private static InstagramService igService;
    private static Verifier verifier;
    private static Scanner scan;
    private static final Token EMPTY_TOKEN = null;
    public String instaURL, twitterURL;
    
    public void TweetCollect(String searchString) throws TwitterException {
        tXCoord = 0;
        tYCoord = 0;
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey("0c1v1wqqNATaVcWpINaHoW3o5")
          .setOAuthConsumerSecret("a6sEAJ1NLY3cXhkMTrMZF62bIfww6qLvlEetDAU9O09Mp04lyh")
          .setOAuthAccessToken("330427831-RJig6B6YycJL3xQVoEjjyRaujwnr1xHEQGtSHOOc")
          .setOAuthAccessTokenSecret("Da71qk4SN6Nu88mLkugDDR6CftVcgOcz3QB8dHNw4hVbD");
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
        query = new Query("#" + searchString);
        result = twitter.search(query);
        for (Status status : result.getTweets()) {
            try {
                System.out.println(status.getGeoLocation());
                System.out.println(status.getCreatedAt());
                tXCoord = status.getGeoLocation().getLatitude();
                tYCoord = status.getGeoLocation().getLongitude();
                twitterString = ("@" + status.getUser().getScreenName() + ": " + status.getText() + " : " + "(" + tXCoord + ", " + tYCoord + ")");
                twitterURL = "https://twitter.com/" + status.getUser().getScreenName() + "/status/" + status.getId();
            }
            catch (Exception e) { }
        } 
    }
    
    public void InstaCollect(String searchString) throws InstagramException, IOException {
        iXCoord = 0;
        iYCoord = 0;
        scan = new Scanner(System.in);
        igService =  new InstagramAuthService()
                .apiKey("4de0532c8b614ac68e54ca3cc13c7b83")
                .apiSecret("99629942c7e24bb08d2e8f328103555a")
                .callback("http://localhost")     
                .build();
        
        authorizationUrl = igService.getAuthorizationUrl(EMPTY_TOKEN);
        System.out.println(authorizationUrl);
        
        verifier = new Verifier(scan.nextLine());
        Token accessToken = igService.getAccessToken(EMPTY_TOKEN, verifier);
        
        Instagram instagram = new Instagram(accessToken); 
        
        TagMediaFeed tagMFeed = instagram.getRecentMediaTags(searchString);
        System.out.println("tagMFeed = " + tagMFeed);
        
        List<MediaFeedData> mediaFeeds = tagMFeed.getData();
        try {
            for(int i = 0; i < 1; i++) {
                for(MediaFeedData mediaData : mediaFeeds) {
                    try {
                        System.out.println(mediaData.getLocation());
                        System.out.println(mediaData.getCreatedTime());
                        Images image = mediaData.getImages();
                        Location location = mediaData.getLocation();
                        creationTime = Integer.parseInt(mediaData.getCreatedTime());
                        
                        if(creationTime > 1414800000) {
                            for(int k = 0; k < 1; k++) {
                                instaURL = mediaData.getLink();
                                iXCoord = location.getLatitude();
                                iYCoord = location.getLongitude();
                            } } }
                    catch (Exception e) {}            
                } } }
        catch (Exception e) {}
    }
    
    public double getTLatitude() {
        return tXCoord;
    }
    
    public double getTLongitude() {
        return tYCoord;
    }
    
    public double getILatitude() {
        return iXCoord;
    }
    
    public double getILongitude() {
        return iYCoord;
    }
    
 }