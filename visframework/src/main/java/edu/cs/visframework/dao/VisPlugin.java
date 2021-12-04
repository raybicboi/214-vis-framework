package edu.cs.visframework.dao;

import edu.cs.visframework.pojo.Value;
import org.json.JSONObject;

import java.util.List;

/**
 * Visualization plugin interface do the following tasks.
 * 1) parse the graph requirement from json field "visSourceName"
 * 2) translate the analyzed data into correct json format for plotly to plot.
 *  2.1) We need to translate Value.time and Value.score into different graph formats.
 *  2.2) The actual format may change since we needs to support different graphs
 *  For example, the heatmap implementation needs to split Value.time into X axis and Y axis, and put Value.score
 *  into Z axis.
 *  For example, the pie chart implementation needs to put the percentage of scores into X
 *  axis.
 *  2.3) The overall idea is, as long as the format is correct(see (3)) and the graph is
 *  meaningful, the implementation is ok.
 *
 * 3) NOTE: the visSourceName and output format needs to correspond with
 * plotly javascript version!
 *  See https://plotly.com/javascript/ for more details.
 *
 * Example input:
 * json: {"visSourceName":"bar"}, analyzedData: [{"2021/11/18", xx}, {"2021/11/19", yy}, ...]
 *
 * Example output:
 * {
 * "layout":{"xaxis":{"title":"Date of Comment","type":"category"},"bargap":0.3,"width":320,"title":"Bar Chart","yaxis":{"range":[0.5,3],"title":"Rate of Sentiment"},"height":240},
 * "data":[{"x":["2021/11/14","2021/11/15","2021/11/16","2021/11/17","2021/11/18","2021/11/19","2021/11/21","2021/11/22"],"y":[1,2.75,2.75,3,2.1666666666666665,3,2.25,2.2],"type":"bar","connectgaps":true}]}]
 * }
 *
 * @author Shicheng Huang
 */
public interface VisPlugin {
    JSONObject getVisedData(JSONObject visSource, List<Value> analyzedData);
}
