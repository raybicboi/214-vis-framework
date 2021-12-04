package edu.cs.visframework.dao;

import com.google.api.client.json.Json;
//import edu.cs.visframework.pojo.Body;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;


import edu.cs.visframework.pojo.Value;
import org.json.JSONObject;

import java.util.List;

/**
 * Data Plugin interface.
 * Our data plugin only do download comment data from data source.
 * We could extend the data source to for example
 * 1) youtube comments
 * 2) twitter posts
 * 3) local files
 * 4) news search
 * ...
 *
 * @author Shicheng Huang
 */
public interface DataPlugin {
    /**
     * Our download interface takes in json format and download data to List<value>.
     * The data plugin needs to do:
     * 1) parse json input
     * 2) download comment(sentence) from data source
     * 3) put the first 100(at most) comment into List<value> with the correct time & sentence content.
     * 4) NOTE: List<value> treat each string comment as atomic and each string comment
     * needs to have its correct posted time for further analysis.
     *
     * Input format: data source Name / Url are two major field the plugin needs to parse.
     * For example, for twitter source, we need to specify user name: SenSchumer inorder to provide
     * enough information to the twitter API to scrap down the data.
     * {
     *  "dataSourceName":"twitter",
     *  "dataSourceUrl":"SenSchumer"
     * }
     * NOTE: The dataSourceUrl is completely dependent on the data plugin implementation,
     * which means that for different sources, url may be different.
     * For example twitter takes in userId for url, youtube takes in youtubeId for url.
     * **This is something the plugin implementer needs to specify to the users.**
     *
     * Output format:
     * List<Value> length at most 100: [{"2021/11/18", xx}, {"2021/11/19", yy}, {"2021/11/20", zz} ...]
     *
     * @param json json format.
     * @return List<value>
     */
    List<Value> download(JSONObject json);
}

