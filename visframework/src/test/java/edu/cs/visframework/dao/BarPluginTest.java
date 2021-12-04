package edu.cs.visframework.dao;

import edu.cs.visframework.pojo.Value;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BarPluginTest {

    @Test
    void getVisedData() {
        List<Value> input = new ArrayList<>();
//        Value v1 = new Value("2000-10-11","", 0);
//        Value v2 = new Value("2000-10-12","", 1);
//        input.add(v1);
//        input.add(v2);

        VisPlugin bar = new BarPlugin();
        JSONObject output = bar.getVisedData(null, input);

        System.out.println(output);
        assertNotNull(output);
    }
}