package edu.cs.visframework.dao;

import edu.cs.visframework.pojo.Value;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterLogicTest {

    @Test
    void getProcessedData() throws JSONException {
        List<Value> input = new ArrayList<>();
        input.add(new Value("2022-12-30", "happy", 2));
        input.add(new Value("2021-11-30", "sad", 1));

        List<Value> input2 = new ArrayList<>();
        input.add(new Value("2021-12-30", "very happy", 3));
        input.add(new Value("2000-11-30", "very sad", 0));

        List<List<Value>> inputList = new ArrayList<>();
        inputList.add(input2);
        inputList.add(input);


        //JSONObject config = new JSONObject({"workSpace":[{"dataSource":[{"dataSourceName":"","dataSourceUrl":""}],"visSource":[{"visSourceName":""}],"processSource":{"filter":{"startDate":"2018-07-01","endDate":"2021-12-31"}}}]});
        JSONObject filter = new JSONObject();
        filter.put("startDate","2018-07-01");
        filter.put("endDate","2021-12-31");
        JSONObject process = new JSONObject();
        process.put("filter",filter);
        JSONObject config = new JSONObject();
        config.put("processSource",process);
        System.out.println(config);

        ProcessLogic filterLogic = new FilterLogic();
        List<Value> output = filterLogic.getProcessedData(inputList, config);

        System.out.println(output);
        assertEquals(output.size(), 2);


    }
}