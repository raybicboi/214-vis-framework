package edu.cs.visframework.dao;

import edu.cs.visframework.pojo.Value;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.json.JsonArray;
import java.util.*;

/**
 * A visulization plugin takes in a list of <Values> and returns a JsonObject of
 * bar graph for the reactive front end to make the plot.
 */
public class BarPlugin implements VisPlugin {

    @Override
    public JSONObject getVisedData(JSONObject visSource, List<Value> analyzedData) {
        /* get two maps:
           scoreSumMap for sum up all scores within the same day.
           countMap count number of scores in a day.*/
        Map<String, Double> scoreSumMap = new TreeMap<String, Double>();
        Map<String, Integer> countMap = new HashMap<>();
        for (Value cur : analyzedData) {
            countMap.put(cur.getTime(), countMap.getOrDefault(cur.getTime(), 0) + 1);
            scoreSumMap.put(cur.getTime(), scoreSumMap.getOrDefault(cur.getTime(), 0.0) + cur.getScore());
        }

        /* x: List of time in String. y: List of scores in double*/
        List<String> x = new ArrayList<String>();
        List<Double> y = new ArrayList<Double>();
        for (String curKey : scoreSumMap.keySet()) {
            x.add(curKey);
            y.add(scoreSumMap.get(curKey)/countMap.get(curKey));
        }

        /* Generate return JsonObject in the following format:
           sample json output {[ {name:"bar", x:[1,2,3], y:[4,5,6]} ]} */
        JSONObject plotJson = new JSONObject();
        if (x.size() == 0) {
            plotJson.put("errorMessage", "There is not data to plot!");
        }

        plotJson.put("type","bar");
        plotJson.put("x", new JSONArray(x));
        plotJson.put("y", new JSONArray(y));
        plotJson.put("connectgaps", true);
//        plotJson.put("mode", "lines+markers");
//        plotJson.put("marker", "red");

        JSONArray graphs = new JSONArray();
        graphs.put(plotJson);

        JSONObject layout = new JSONObject();
        JSONObject xaxis = new JSONObject();
//        xaxis.put("title", "Date of Comment");
        xaxis.put("type", "category");

        JSONObject yaxis = new JSONObject();
        yaxis.put("title", "Rate of Sentiment");
        float min_max[] = new float[2];
        Optional q = y.stream().min(Double::compare);
        min_max[0] = q.isPresent() ? ((Double) q.get()).floatValue() / 2 : 0;
        Optional q2 = y.stream().max(Double::compare);
        min_max[1] = q2.isPresent() ? ((Double) q2.get()).floatValue() : 0;

        yaxis.put("range", min_max);

        layout.put("xaxis", xaxis);
        layout.put("yaxis", yaxis);
        layout.put("width", 320);
        layout.put("height", 240);
        layout.put("title", "Bar Chart");
        layout.put("bargap", 0.3);

        JSONObject result = new JSONObject();
        result.put("data", graphs);
        result.put("layout", layout);

        return result;
    }
}
