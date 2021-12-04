package edu.cs.visframework.dao;

import edu.cs.visframework.pojo.Value;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PiePluginTest {

    @Test
    public void testGraph() throws JSONException {
        List<Value> input = new ArrayList<>();
        Value v1 = new Value("0000000000","", 0);
        Value v2 = new Value("0000000000","", 1);
        input.add(v1);
        input.add(v2);

        PiePlugin pie = new PiePlugin();
        JSONObject json = pie.getVisedData(null, input);

        assertEquals(json.getJSONArray("data").getJSONObject(0).getJSONArray("values"), new JSONArray(new ArrayList<>(Arrays.asList(50,50,0,0,0))));
    }
}