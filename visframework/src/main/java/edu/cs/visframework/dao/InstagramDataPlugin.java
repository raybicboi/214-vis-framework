package edu.cs.visframework.dao;

import edu.cs.visframework.pojo.Value;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mortbay.util.ajax.JSON;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class InstagramDataPlugin implements DataPlugin {
    private String API_HOST = "instagram-data1.p.rapidapi.com";
    private String API_KEY = "93e5ac0183msh4a9a09856232441p1569c6jsnd5c66c90f837";
    private String BASE_URL = "https://instagram-data1.p.rapidapi.com/";
    private String POST_URL_HEAD = "user/feed?username=";
    private String HASH_URL_HEAD = "hashtag/feed?hashtag=";
    private String COMMENT_URL_HEAD = "comments?post=";
    private int MAX_DATA = 40;

    /**
     * Produces a list of {@code Value} that can be analyzed.
     * @param config - contains 'dataSourceUrl' attribute. The value
     *               mapped to by this key is either an Instagram handle
     *               (with @)
     *               or hashtag (with or without #)
     * @return List of values.
     */
    @Override
    public List<Value> download(JSONObject config) {
        String toUse = config.getString("dataSourceUrl");

        if (toUse == null || toUse.length() < 1)
            return null;

        String firstChar = toUse.substring(0,1);
        String rest = toUse.substring(1);

        List<Value> res;
        switch (firstChar) {
            case "@":
                var x = buildRequest(POST_URL_HEAD,rest);
                res = usernameValBuilder(getCollectorArray(x));
                break;
            case "#":
                var y = buildRequest(HASH_URL_HEAD,rest);
                res = hashtagValBuilder(getCollectorArray(y));
                break;
            default:
                var z = buildRequest(HASH_URL_HEAD,toUse);
                res = hashtagValBuilder(getCollectorArray(z));
                break;
        }

        return res;
    }

    /**
     * Gets the string which can be used to obtain a collector for
     * the comments under a post.
     * @param postFromCollection - a post json object with the attribute
     *                           'shortcode'. e.g.
     *                           https://www.instagram.com/p/CAVeEm1gDh2/
     *                          produces the following shortcode:
     *
     *                                      'CAVeEm1gDh2'
     * @return a string which can be interpreted as JSON containing
     * info about a posts' comments.
     */
    private String getCommentsFromPost(JSONObject postFromCollection) {
        String shortcode = postFromCollection.getString("shortcode");
        //gets the comments from a post
        return buildRequest(COMMENT_URL_HEAD, shortcode);
    }

    /**
     * Given a JSONArray which collects info on all posts,
     * returns a list of values built from the comments under those posts.
     * @param allPostData
     * @return List of {@link Value}.
     */
    private List<Value> usernameValBuilder(JSONArray allPostData) {
        if (allPostData.isEmpty()) {
            System.out.println("No Data to Display");
            return new ArrayList<>();
        }

        int m = Math.min(allPostData.length(), MAX_DATA);
        List<Value> res = new ArrayList<>();
        List<JSONObject> posts = new ArrayList<>();

        for (int j = 0; j < m; j++) {
            JSONObject post = allPostData.getJSONObject(j);
            posts.add(post);
        }

        //builds the values from each posts' comments.
        List<List<Value>> commentsFromEachPost;
        commentsFromEachPost = posts.parallelStream()
                .map(x -> getValsFromComments(getCollectorArray
                        (getCommentsFromPost(x))))
                .collect(Collectors.toList());
        commentsFromEachPost.parallelStream().forEach(x -> res.addAll(x));

        return fixDataSize(res);
    }

    /**
     * Builds the values from a JSONArray containing JSONObjects that
     * represent Instagram comments under a single post.
     * @param comments - JSONArray containing objects with the attribute
     *                 'text'. These objects get the text from instagram values.
     *                 They must have the attribute 'created_at' as well.
     * @return the list of values built from the comments
     */
    private List<Value> getValsFromComments(JSONArray comments) {
        List<Value> res = new ArrayList<>();
        for (int i = 0; i < Math.min(comments.length(),MAX_DATA) ; i++) {
            JSONObject j = comments.getJSONObject(i);
            String textQ = j.getString("text");
            String text = textQ.length() < 130 ? textQ : textQ.substring(0,129);

            res.addAll(filterAndBuild(j, text, false));
        }
        return fixDataSize(res);
    }

    /**
     * Builds a value. Returns it in a list.
     * @param j - either a post or a comment, as JSONObject.
     * @param text - raw text to be added to value
     * @param hashtag - true if this is from a hashtag query, false otherwise.
     * @return List of built {@link Value} containing only English letters and
     *         spaces in the text field. Will only contain 0 or 1 values.
     */
    private List<Value> filterAndBuild(JSONObject j,String text,Boolean hashtag) {
        List<Value> container = new ArrayList<>();
        String filteredText = text.codePoints()
                .filter(ch -> ch >= 'A' && ch <= 'Z'
                        || ch >= 'a' && ch <= 'z'
                        || ch == ' '
                        || ch == '#')
                .mapToObj(x -> Character.toString(x) == "#" ? " " : Character.toString(x))
                .collect(Collectors.joining());
        int z = 0;
        for (int u = 0; u < filteredText.length(); u++) {
            if (filteredText.substring(u).equals(" "))
                z--;
        }
        String textFinal =  filteredText.length() < 80 ? filteredText : filteredText.substring(0,79);

        if (z == 0 && !filteredText.isEmpty()) {
            container.add(new Value(getDateString(j,hashtag),textFinal,2));
        }
        return container;
    }

    /**
     * Builds values with a collector that has information for a specific
     * hashtag.
     * @param hashtagPosts - a collector array that has posts with
     *                  'taken_at_timestamp' and 'description' attributes.
     * @return List of values built from THE CAPTION of the post(s)
     *         under this hashtag.
     */
    private List<Value> hashtagValBuilder(JSONArray hashtagPosts) {
        if (hashtagPosts.isEmpty()) {
            System.out.println("No Data to Display");
            return new ArrayList<>();
        }
        int m = hashtagPosts.length() < MAX_DATA ? hashtagPosts.length() : MAX_DATA;
        List<Value> res = new ArrayList<>();
        for (int j = 0; j < m; j++) {
            JSONObject oj = hashtagPosts.getJSONObject(j);
            String desc = ((String) oj.get("description"));
            res.addAll(filterAndBuild(oj, desc,true));
        }
        return fixDataSize(res);
    }

    /**
     * Fixes the size of {@param res} to within the MAX_DATA range.
     * @param res
     * @return The fixed result.
     */
    private List<Value> fixDataSize(List<Value> res) {
        if (res == null || res.isEmpty())
            return res;
        return res.subList(0, Math.min(res.size() - 1, MAX_DATA));
    }

    /**
     * Gets the string representing the date for the given JSONObject.
     * @param oj
     * @param hashtag - true is the query concerns a hashtag (with or without
     *                the inclusion of # is fine)
     * @return the string representing the date in yyyy-MM-dd format.
     */
    private String getDateString(JSONObject oj, boolean hashtag) {
        int stamp = 0;
        //if we're dealing with a # query, we're getting data from a post
        if (hashtag)
            stamp = getStampPost(oj);
        //if we're dealing with an @ query, we're getting data from a comment
        if (!hashtag)
            stamp = getStampComment(oj);
        Date date = new Date(stamp * 1000L);
        SimpleDateFormat jdf =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        String java_date = jdf.format(date);
        return java_date;
    }

    /**
     * Given an object with attribute 'created_at', gets the unix
     * timestamp.
     * @param oj
     * @return unix timestamp, not in milliseconds.
     */
    private int getStampComment(JSONObject oj) {
        return oj.getInt("created_at");
    }

    /**
     * Given an object with attribute 'taken_at_timestamp', gets the unix
     * timestamp.
     * @param oj
     * @return unix timestamp, not in milliseconds.
     */
    private int getStampPost(JSONObject oj) {
        return oj.getInt("taken_at_timestamp");
    }

    /**
     * Given a string which can be interpreted as json, returns the
     * JSONArray which results from extracting the 'collector' attribute
     * for {@param jsonInDisguise}
     * @param jsonInDisguise
     * @return the JSONArray of the collector.
     */
    private JSONArray getCollectorArray(String jsonInDisguise) {
        HashMap<String,String> j =
                (HashMap<String, String>) JSON.parse(jsonInDisguise);
        JSONObject json = new JSONObject(j);
        return json.getJSONArray("collector");
    }

    /**
     * Makes an api request, adding {@param head} and {@param tail} to
     * the base URL for the API call.
     * @param head
     * @param tail
     * @return the body of the HTML response, as a String.
     */
    private String buildRequest(String head, String tail) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + head + tail))
                .header("x-rapidapi-host", API_HOST)
                .header("x-rapidapi-key", API_KEY)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response.body();
    }
}