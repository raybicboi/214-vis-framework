package edu.cs.visframework.dao;

import edu.cs.visframework.pojo.Value;
import org.json.JSONObject;

import java.util.List;

public class TestDataPlugin implements DataPlugin {

    @Override
    public List<Value> download(JSONObject json) {
        return null;
    }
}
