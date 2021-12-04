package edu.cs.visframework.dao;

import edu.cs.visframework.pojo.Value;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class YtDataPluginTest {

    @Test
    public void testGetComment() throws JSONException {
        JSONObject input = new JSONObject();
        input.put("dataSourceUrl", "hPd8JjJ8IJA");

        YtDataPlugin youtube = new YtDataPlugin();
        List<Value> output = youtube.download(input);
        System.out.println(output);
        assertTrue(output.size()>0);
    }

}