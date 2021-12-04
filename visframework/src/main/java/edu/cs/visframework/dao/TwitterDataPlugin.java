package edu.cs.visframework.dao;

import edu.cs.visframework.pojo.Value;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * The twitter plugin takes in a username, and returns a list of </Value>
 * consistes of content of posts and dates of postes for the selected user.
 * Reference from twitter API's offical samples:
 * https://github.com/twitterdev/Twitter-API-v2-sample-code
 */
public class TwitterDataPlugin implements DataPlugin{
    /* Developer user token to access dataAPI*/
    private static final String BEARER_TOKEN = "AAAAAAAAAAAAAAAAAAAAAKmoVwEAAAAAC%2FH1SqhI4TqcOS%2BWhOVuPGHyoS0%3DtmwYwoBuSNBxwXPs3lnTuc0xK5vQ1zkV1rTaonxL2jScE7fgax";

    public static void main(String args[]) throws IOException, URISyntaxException {
        DataPlugin twitterPlugin = new TwitterDataPlugin();
        List returnArrary = twitterPlugin.download(null);
        System.out.println(returnArrary.toString());
    }

    /**
     * Download Tweet posts from the selected user from Twitter API and return
     * a list of posts.
     * @param config JSONObject from front end with twitter user name
     * @return list of </Value>
     */
    @Override
    public List<Value> download(JSONObject config) {
        /* Developer user token to access dataAPI*/

//        String userName = config.getString("dataSourceUrl");
        String userName = "elonmusk"; // "YHu472"

        if (null != this.BEARER_TOKEN) {
            // Replace with user ID below
            String tweetsResponse = null;
            try {
                String userID = getUsers(userName, this.BEARER_TOKEN);
//                System.out.println("\n *********** ID: " + userID + "************ \n");
                tweetsResponse = getTweets(userID, this.BEARER_TOKEN);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                config.put("errorMessage", "Invalid twitter user name.");
                return null;
            }

            if (tweetsResponse == null) {
                config.put("errorMessage", "Fail to get tweets for the selected user name.");
                return null;
            }

            System.out.println(tweetsResponse);
            System.out.println(" *** Twitter GET Response successfully finished. ***");

            JSONObject result = new JSONObject(tweetsResponse); //Convert String to JSON Object
            JSONArray tokenList = result.getJSONArray("data");
            List<Value> returnValueList = new ArrayList<Value>();

            if (tokenList.length() == 0) {
                config.put("errorMessage", "The selected user hasn't post any tweets yet.");
                return null;
            }

            for (int i = 0; i < tokenList.length(); i++) {
                // Read JsonObject from response json file to get text and time.
                JSONObject oj = tokenList.getJSONObject(i);
                String postText = oj.getString("text");
                String postTime = oj.getString("created_at");

                returnValueList.add(new Value(postTime, postText, 2));
                System.out.println("text : " + postText + "  ,time :" + postTime);
            }

            return returnValueList;
        } else {
            System.out.println("There was a problem getting your bearer token. Please make sure you set the BEARER_TOKEN environment variable");
            config.put("errorMessage", "There was a problem getting your bearer token.");
            return null;
        }
    }

    /**
     * This method calls the v2 Users endpoint with usernames as query parameter
     * @param usernames
     * @param bearerToken
     * @return userID from username in String format
     * @throws IOException
     * @throws URISyntaxException
     */
    private static String getUsers(String usernames, String bearerToken) throws IOException, URISyntaxException {
        String userResponse = null;
        String userID = null;
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/users/by");
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("usernames", usernames));
        queryParameters.add(new BasicNameValuePair("user.fields", "created_at,description,pinned_tweet_id"));
        uriBuilder.addParameters(queryParameters);

        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
        httpGet.setHeader("Content-Type", "application/json");

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (null != entity) {
            userResponse = EntityUtils.toString(entity, "UTF-8");
            JSONObject userDataResp = new JSONObject(userResponse);
            if (userDataResp.has("data")) {
                JSONArray dataArray = userDataResp.getJSONArray("data");
                JSONObject dataUser = dataArray.getJSONObject(0);
                userID = dataUser.getString("id");
            }
        }
        return userID;
    }

    /**
     * This method calls the v2 User Tweet timeline endpoint by user ID
     * @param userId
     * @param bearerToken
     * @return JSON object in string format
     * @throws IOException
     * @throws URISyntaxException
     */
    private static String getTweets(String userId, String bearerToken) throws IOException, URISyntaxException {
        if (userId == null) {
            return null;
        }

        String tweetResponse = null;

        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        URIBuilder uriBuilder = new URIBuilder(String.format("https://api.twitter.com/2/users/%s/tweets", userId));
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("tweet.fields", "created_at"));
        queryParameters.add(new BasicNameValuePair("max_results", "100"));
        uriBuilder.addParameters(queryParameters);

        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
        httpGet.setHeader("Content-Type", "application/json");

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (null != entity) {
            tweetResponse = EntityUtils.toString(entity, "UTF-8");
        }
        return tweetResponse;
    }
}

