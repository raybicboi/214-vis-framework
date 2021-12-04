package edu.cs.visframework.dao;

import edu.cs.visframework.pojo.Value;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TwitterDataPluginTest {

    @Test
    void download() throws JSONException {
        JSONObject input = new JSONObject();
        input.put("dataSourceUrl", "YHu472"); // elonmusk

        TwitterDataPlugin twitterPlug = new TwitterDataPlugin();
        List<Value> output = twitterPlug.download(input);
        System.out.println(output);
        assertTrue(output.size()>0);
    }

    @Test
    void downloadFail() throws JSONException {
        JSONObject input = new JSONObject();
        input.put("dataSourceUrl", "**nobody&&");

        TwitterDataPlugin twitterPlug = new TwitterDataPlugin();
        List<Value> output = twitterPlug.download(input);
        assertNull(output);
    }
}