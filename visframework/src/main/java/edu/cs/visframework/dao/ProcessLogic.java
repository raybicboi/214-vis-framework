package edu.cs.visframework.dao;

import edu.cs.visframework.pojo.Value;
import org.json.JSONObject;

import java.util.List;

/**
 * The process Logic focuses on the processing of data.
 * For example
 * 1) filtering data sources with start/ end date.
 * 2) aggregating data sources.
 * 3) ...
 *
 * @author Shicheng Huang
 */
public interface ProcessLogic {
    /**
     * We take in a list of data sources and return one "processed" data source.
     * For example,
     * 1) aggregation aggregates the list of data sources into one single list.
     *
     * @param input
     * @param config
     * @return
     */
    List<Value> getProcessedData(List<List<Value>> input, JSONObject config);
}
