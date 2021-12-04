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
import java.util.Map;

public class NewsDataPlugin implements DataPlugin{
    private static final String KEY = "8a00d6f4876c428089a316f739b6164a";

    public static void main(String args[]) throws IOException, URISyntaxException {
        DataPlugin newsPlugin = new NewsDataPlugin();
        List returnArray = newsPlugin.download(null);
//        System.out.println(returnArray.toString());
    }


    @Override
    public List<Value> download(JSONObject config) {
        String newsResponse = null;

//        String newsName = config.getString("dataSourceUrl");
        String newsName = "cmu";

        try {
            newsResponse = getNews(newsName);
        } catch (URISyntaxException |IOException  e) {
            config.put("errorMessage", "Invalid News search token.");
            return null;
        }

        if (newsResponse == null) {
            config.put("errorMessage", "Fail to get response from News API.");
            return null;
        }

        JSONObject result = new JSONObject(newsResponse); //Convert String to JSON Object
        JSONArray newsList = result.getJSONArray("articles");

        if (newsList.length() == 0) {
            config.put("errorMessage", "News API could not find any news related to the given topic.");
            return null;
        }

        List newsReturnList = new ArrayList<Value>();

        for (int i = 0; i < newsList.length(); i++) {
            JSONObject oj = newsList.getJSONObject(i);
            String postText = oj.getString("description");
            String postTime = oj.getString("publishedAt");
            System.out.println("text" + postText + ", time " + postTime);
            newsReturnList.add(new Value(postTime, postText, 2));

        }
        return newsReturnList;
    }

    /**
     * Return newsList JsonObject in a String Format. newList contains 20 news.
     * @param topic
     * @return response from news
     * @throws URISyntaxException
     * @throws IOException
     */
    private String getNews(String topic) throws URISyntaxException, IOException {
        String newsResponse = null;

        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();
        URIBuilder uriBuilder = new URIBuilder(String.format("https://newsapi.org/v2/everything?q=%s&apiKey=%s",
                topic, this.KEY));
        HttpGet httpGet = new HttpGet(uriBuilder.build());

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (null != entity) {
            newsResponse = EntityUtils.toString(entity, "UTF-8");
//            System.out.println(newsResponse);
        }
        return newsResponse;
    }
}
