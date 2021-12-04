package edu.cs.visframework.dao;

import edu.cs.visframework.pojo.Value;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class TimeSeriesPlugin implements VisPlugin {

    private List<String> months;
    private List<String> sentiments;

    /**
     * Constructor for Time Series Plugin. Initializes the arrays containing
     * the months and the names of the possible sentiments.
     */
    public TimeSeriesPlugin() {
        months = new ArrayList<>(Arrays.asList(
                "January", "February", "March",
                "April", "May", "June",
                "July", "August", "September",
                "October", "November", "December"
        ));

        sentiments = new ArrayList<>(Arrays.asList(
                "Very Negative", "Negative", "Neutral",
                "Positive", "Very Positive"
        ));
    }

    /**
     * time series plugin
     * @param visSource
     * @param analyzedData list of values that have been analyzed
     * @return JSONObject: traces for each sentiment
     *                         x-axis months
     *                         y value percentage of total sentiment value
     */
    @Override
    public JSONObject getVisedData(JSONObject visSource,
                                   List<Value> analyzedData){
        List<List<Integer>> values = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            values.add(new ArrayList<>(Arrays.asList(
                    0,0,0,0,0,0,0,0,0,0,0,0
            )));
        }

        updateVals(analyzedData, values);
        List<List<Integer>> portions = new ArrayList<>();
        getPercentageData(values, portions);

        Map<String, List<Integer>> valFinal = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            valFinal.put(sentiments.get(i), portions.get(i));
        }

        List<JSONObject> l = addTraces();
        buildTraces(l,analyzedData,valFinal);

        if (analyzedData.isEmpty())
            return l.get(0);

        return getResult(l);
    }

    /**
     * Given a list of JSON objects, builds a layout json object and
     * returns a json object containing the layout and {@param l} treated
     * as the data for visualization. In this case it's a list of traces.
     * @param l
     * @return the final JSON object that can be used by Plotly
     */
    private JSONObject getResult(List<JSONObject> l) {
        JSONObject layout = new JSONObject();
        layout.put("width", 320);
        layout.put("height", 240);
        layout.put("title", "Sentiments per Month (by %)");
        JSONObject legend = new JSONObject();
        legend.put("size", 25);
        layout.put("legend",legend);
        JSONObject result = new JSONObject();
        result.put("data", new JSONArray(l));
        result.put("layout", layout);
        return result;
    }

    /**
     * Builds the traces  (inserts the data for each one).
     * @param traces
     * @param analyzedData
     * @param valFinal
     */
    public void buildTraces(List<JSONObject> traces, List<Value> analyzedData,
                            Map<String,List<Integer>> valFinal) {
        int i = 0;
        for (JSONObject j : traces) {
            if (analyzedData.size() == 0){
                j.put("errorMessage", "No data to plot");
                i++;
                continue;
            }
            j.put("type", "scatter");
            j.put("mode", "lines");
            j.put("name", sentiments.get(i));
            j.put("x",new JSONArray(months));
            j.put("y", new JSONArray(valFinal.get(sentiments.get(i))));
            i++;
        }
    }

    /**
     * Adds 5 traces, one for each sentiment
     * @return a list of {@JSONObject} which are a trace per sentiment,
     * containing no info currently.
     */
    private List<JSONObject> addTraces() {
        JSONObject trace1 = new JSONObject();
        JSONObject trace2 = new JSONObject();
        JSONObject trace3 = new JSONObject();
        JSONObject trace4 = new JSONObject();
        JSONObject trace5 = new JSONObject();
        List<JSONObject> l = new ArrayList<>();
        l.add(trace1);
        l.add(trace2);
        l.add(trace3);
        l.add(trace4);
        l.add(trace5);
        return l;
    }

    /**
     * maps each data point to its percentage of the total.
     * @param values - data points to map over
     * @param portions - the resulting nested list with percentage data to be
     *                   added.
     */
    private void getPercentageData(List<List<Integer>> values,
                                   List<List<Integer>> portions) {
        int sum = 0;
        for (List<Integer> v : values) {
            sum += v.stream().reduce(0,
                    (subtotal, element) -> subtotal + element);
        }
        for (List<Integer> v : values) {
            if (sum > 0)
                portions.add(currPercentages(v,sum));
            else {
                portions.add(v.stream().map(x -> 0)
                        .collect(Collectors.toList()));
                System.out.println("All Zeroes");
            }
        }
    }

    /**
     * Extracts the analyzed data and updates {@param values}
     * @param analyzedData - list of analyzed data ({@code Value}
     * @param values - list of lists of ints, one list for each month.
     */
    private void updateVals(List<Value> analyzedData,
                            List<List<Integer>> values) {
        for (Value v: analyzedData){
            int month = Integer.parseInt(v.getTime().substring(5,7));
            int sentiment = (int) Math.round(v.getScore());

            int oldValue = values.get(sentiment).get(month-1);
            values.get(sentiment).set(month-1, oldValue+1);
        }
    }

    /**
     * Maps each value in {@param v} to its proportion of {@param sum}.
     * @param v
     * @param sum
     * @return the updated list of integers.
     */
    public List<Integer> currPercentages(List<Integer> v, int sum) {
        return v.stream().map(x -> (int) (((double) x) * 100 / sum))
                .collect(Collectors.toList());
    }

}