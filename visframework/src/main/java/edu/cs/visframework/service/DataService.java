package edu.cs.visframework.service;


import edu.cs.visframework.dao.DataPlugin;
import edu.cs.visframework.dao.NlpLogic;
import edu.cs.visframework.dao.ProcessLogic;
import edu.cs.visframework.dao.VisPlugin;
import edu.cs.visframework.pojo.Value;
import edu.cs.visframework.utils.Stopwatch;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the data service class for MVC pattern.
 * Data service process data from input Json format then
 * 0) parse input json object
 * 1) allocate data plugins
 * 2) download data using data plugins
 * 3) allocate process plugins
 * 4) process data using process plugins (filter)
 * 5) allocate NLP plugins
 * 6) process data using ML algorithms
 * 7) allocate visualization plugins
 * 8) load data to JSON object with the correct format for plotting.
 *
 * NOTE: Attached an example json we used for input.
 * new_json: {"workSpace":[{"dataSource":[{"dataSourceName":"twitter","dataSourceUrl":"SenSchumer"}],"visSource":[{"visSourceName":"heatmap"},{"visSourceName":"bar"}],"processSource":{"filter":{"startDate":"2017-01-08","endDate":"2021-11-22"}}}]}
 *
 * NOTE: Attached an example json we used for output.
 * {"graph":[{"name":"heatmap","x":["January","February","March","April","May","June","July","August","September","October","November","December"],"y":["Very Negative","Negative","Neutral","Positive","Very Positive"],"z":[[0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,3,0],[0,0,0,0,0,0,0,0,0,0,11,0],[0,0,0,0,0,0,0,0,0,0,6,0],[0,0,0,0,0,0,0,0,0,0,0,0]]},{"name":"bar","x":["2021/11/18","2021/11/19","2021/11/20","2021/11/21","2021/11/22"],"y":[2.5,1.8571428571428572,2.375,1.777777777777778,1.6666666666666667]}]}
 *
 * @author Shicheng Huang
 */
@Service
public class DataService {
    @Resource
    private ParserService parserService;

    /**
     * Brief:
     * This function returns the processed data in json object which represent a graph.
     * @param json input json (see class java doc for example).
     * @return returned json.
     */
    public JSONObject getProcessedData(JSONObject json) throws IOException, URISyntaxException {
        // The returned json.
        JSONObject result = new JSONObject();
        Stopwatch stop = new Stopwatch();

        // We have multiple workspaces.
        for (int i = 0; i <= parserService.getWorkSpaceCount(json) - 1; i++) {
            // Get workspace.
            JSONObject workSpace = parserService.getWorkSpace(json, i);

            // Download data to the list.
            List<List<Value>> dataDownloaded = new ArrayList<>();
            for (int j = 0; j <= parserService.getDataPluginCount(workSpace) - 1; j++) {
                // Get data plugin.
                JSONObject dataSource = parserService.getDataSource(workSpace, j);
                DataPlugin dataPlugin;
                try {
                    dataPlugin = parserService.getDataPlugin(workSpace, j);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return createErrorReturn("Not supported Data Source.");
                }

                // Download data source.
                List<Value> downloaded = dataPlugin.download(dataSource);
                if (downloaded != null) {
                    dataDownloaded.add(downloaded);
                }
            }

            // Process data.
            ProcessLogic processLogic = parserService.getProcessLogic(workSpace);
            List<Value> processedData = processLogic.getProcessedData(dataDownloaded, workSpace);

            // Sentimental analysis.
            NlpLogic nlpLogic = parserService.getNlplogic(workSpace);
            List<Value> analyzedData = nlpLogic.getAnaylzedData(processedData);

            // Visualization.
            List<JSONObject> visValue = new ArrayList<>();
            for (int j = 0; j <= parserService.getVisPluginCount(workSpace) - 1; j++) {
                JSONObject visSource = parserService.getVisSource(workSpace, j);
                VisPlugin visPlugin;
                try {
                    visPlugin = parserService.getVisPlugin(workSpace, j);
                } catch (Exception e) {
                    return createErrorReturn("Not supported Graph Type.");
                }

                JSONObject visedData = visPlugin.getVisedData(visSource, analyzedData);
                visValue.add(visedData);
            }
            collectVisedResult(result, visValue, json);
        }
        System.out.println(result);
        return result;
    }

    /**
     * Helper function to create empty return json.
     * @param error error message.
     * @return json object.
     */
    private JSONObject createErrorReturn(String error) {
        JSONObject emptyReturn = new JSONObject();
        emptyReturn.put("errorMessage", error);
        return emptyReturn;
    }

    /**
     * Helper function to collect data into return.
     * @param result
     * @param visedData
     */
    private void collectVisedResult(JSONObject result, List<JSONObject> visedData, JSONObject json) {
        JSONArray jsonArr = new JSONArray();
        for (JSONObject single: visedData) {
            jsonArr.put(single);
        }

        String error;
        try {
            error = json.getString("errorMessage");
            result.put("errorMessage", error);
        } catch (JSONException ignored) {
        }

        result.put("graph", jsonArr);
    }


}
