package edu.cs.visframework.dao;

import edu.cs.visframework.pojo.Value;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HeatMapPluginTest {
    @Test
    public void testGraph() throws JSONException {
        List<Value> input = new ArrayList<>();
        Value v1 = new Value("2000-10-11","", 0);
        Value v2 = new Value("2000-10-11","", 1);
        input.add(v1);
        input.add(v2);

        HeatMapPlugin heatmap = new HeatMapPlugin();
        JSONObject output = heatmap.getVisedData(null, input);

        List<List<Integer>> refOutput = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            refOutput.add(new ArrayList<>(Arrays.asList(
                    0,0,0,0,0,0,0,0,0,0,0,0
            )));
        }

        refOutput.get(0).set(9, 1);
        refOutput.get(1).set(9, 1);

        assertEquals(output.getJSONArray("data").getJSONObject(0).getJSONArray("z"), new JSONArray(refOutput));

    }
}