package edu.cs.visframework.dao;

import edu.cs.visframework.pojo.Value;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterLogic implements ProcessLogic{

    @Override
    public List<Value> getProcessedData(List<List<Value>> input, JSONObject config) {
//        String stTime = "2000-01-01";
//        String edTime = "2022-01-01";
//        Sample config
//        {"workSpace":[{"dataSource":[{"dataSourceName":"","dataSourceUrl":""}],"visSource":[{"visSourceName":""}],
//        "processSource":{"filter":{"startDate":"2018-07-01","endDate":"2021-12-31"}}}]}

        String stTime = config.getJSONObject("processSource").getJSONObject("filter").getString("startDate").replace("-","/");
        String edTime = config.getJSONObject("processSource").getJSONObject("filter").getString("endDate").replace("-","/");


        List<Value> mergedList = dataMerge(input);
        List<Value> processedList = dataSortFilter(mergedList,stTime, edTime);
        return processedList;
    }


    private List<Value> dataMerge(List<List<Value>> input) {
        List<Value> mergedList = new ArrayList<>();
        for (List<Value> currentList : input ) {
            mergedList.addAll(currentList);
        }
        return mergedList;
    }

    private List<Value> dataSortFilter(List<Value> input, String startTime, String endTime) {
        Collections.sort(input);
        /* Remove data less than start time & data more than end time.*/
        input.removeIf(data -> (data.getTime().compareTo(endTime) > 0));
        input.removeIf(data -> (data.getTime().compareTo(startTime) < 0));
        return input;
    }



}
