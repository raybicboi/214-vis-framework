package edu.cs.visframework.dao;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentSnippet;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import edu.cs.visframework.pojo.Value;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

/**
 * YouTube data plugin,
 * some code from https://developers.google.com/youtube/v3/docs/commentThreads/list
 */
public class YtDataPlugin implements DataPlugin {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String DEVELOPER_KEY = "AIzaSyAkLvwROIXPA6J-roDJ6nSNnR8Nrk_PB00";

    /**
     * download 20 comments from a certain video
     * @param config JSONObject, get video id from dataSourceUrl
     * @return list of Value
     */
    @Override
    public List<Value> download(JSONObject config) {
        String videoId = config.getString("dataSourceUrl");
        List<Value> data = new ArrayList<>();
        try{
            YouTube youtubeService = getService();
            // Define and execute the API request
            YouTube.CommentThreads.List request = youtubeService.commentThreads()
                    .list("snippet,replies");
            CommentThreadListResponse response = request.setKey(DEVELOPER_KEY)
                    .setVideoId(videoId)
                    .execute();
            List<CommentThread> comments = response.getItems();
            for (CommentThread comment:comments){
                CommentSnippet snippet = comment.getSnippet().getTopLevelComment().getSnippet();
                String commentText = snippet.getTextOriginal();
                String time = snippet.getPublishedAt().toString();
                Value dataEntry = new Value(time, commentText, 2);
                data.add(dataEntry);
            }
        } catch (GeneralSecurityException | IOException e) {
            config.put("errorMessage", "Invalid request, try again and input correct youtube video id!");
            return null;
        }

        if (data.size() == 0){
            config.put("errorMessage", "No comment found in this video, try another one!");
            return null;
        }

        return data;
    }

    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    private static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                .setApplicationName("test")
                .build();
    }


}
