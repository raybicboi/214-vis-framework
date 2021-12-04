package edu.cs.visframework.dao;

import edu.cs.visframework.pojo.Value;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PiePlugin implements VisPlugin{
    /**
     * pie plugin
     * @param visSource
     * @param analyzedData list of Value
     * @return JSONObject : labels sentiments
     *                      values portion of certain sentiment
     */
    @Override
    public JSONObject getVisedData(JSONObject visSource, List<Value> analyzedData){
        JSONObject graph = new JSONObject();
        if (analyzedData.size() == 0){
            graph.put("errorMessage", "No data to plot");
            return graph;
        }

        graph.put("type", "pie");

        List<String> labels = new ArrayList<>(Arrays.asList(
                "Very Negative", "Negative", "Neutral", "Positive", "Very Positive"
        ));
        graph.put("labels", new JSONArray(labels));

        List<Double> values = new ArrayList<>(Arrays.asList(0.0,0.0,0.0,0.0,0.0));
        for (Value v:analyzedData){
            int sentiment = (int) Math.round(v.getScore());
            double oldValue = values.get(sentiment);
            values.set(sentiment, oldValue+1);
        }

        List<Integer> portions = values.stream().map(
                (x)->{
                    return (int) Math.round(x * 100 / analyzedData.size());
                }).collect(Collectors.toList());

        graph.put("values", new JSONArray(portions));

        JSONArray graphs = new JSONArray();
        graphs.put(graph);

        JSONObject layout = new JSONObject();
        layout.put("width", 320);
        layout.put("height", 240);
        layout.put("title", "Pie Chart");

        JSONObject result = new JSONObject();
        result.put("data", graphs);
        result.put("layout", layout);

        return result;
    }
}
