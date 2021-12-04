package edu.cs.visframework.dao;

import edu.cs.visframework.pojo.Value;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.json.Json;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeatMapPlugin implements VisPlugin{
    /**
     * heatmap plugin
     * @param visSource
     * @param analyzedData list of value
     * @return JSONObject: x-axis months
     *                     y-axis sentiment
     *                     z-axis(value) num of certain (month, sentiment) state
     */
    @Override
    public JSONObject getVisedData(JSONObject visSource, List<Value> analyzedData){
        JSONObject graph = new JSONObject();
        if (analyzedData.size() == 0){
            graph.put("errorMessage", "No data to plot");
            return graph;
        }
        graph.put("type", "heatmap");
        graph.put("hoverongaps", false);

        List<String> xLabel = new ArrayList<>(Arrays.asList(
           "January", "February", "March",
           "April", "May", "June",
           "July", "August", "September",
           "October", "November", "December"
        ));
        graph.put("x", new JSONArray(xLabel));

        List<String> yLabel = new ArrayList<>(Arrays.asList(
                "Very Negative", "Negative", "Neutral", "Positive", "Very Positive"
        ));
        graph.put("y", new JSONArray(yLabel));

        List<List<Integer>> values = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            values.add(new ArrayList<>(Arrays.asList(
                    0,0,0,0,0,0,0,0,0,0,0,0
            )));
        }

        for (Value v:analyzedData){
            int month = Integer.parseInt(v.getTime().substring(5,7));
            int sentiment = (int) Math.round(v.getScore());

            int oldValue = values.get(sentiment).get(month-1);
            values.get(sentiment).set(month-1, oldValue+1);
        }
        graph.put("z", new JSONArray(values));

        JSONArray graphs = new JSONArray();
        graphs.put(graph);

        JSONObject layout = new JSONObject();
        layout.put("width", 320);
        layout.put("height", 240);
        layout.put("title", "Heat Map");

        JSONObject result = new JSONObject();
        result.put("data", graphs);
        result.put("layout", layout);

        return result;
    }
}
