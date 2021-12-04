package edu.cs.visframework.service;

import edu.cs.visframework.dao.BarPlugin;
import edu.cs.visframework.dao.DataPlugin;
import edu.cs.visframework.dao.FilterLogic;
import edu.cs.visframework.dao.HeatMapPlugin;
import edu.cs.visframework.dao.NewsDataPlugin;
import edu.cs.visframework.dao.NlpLogic;
import edu.cs.visframework.dao.PiePlugin;
import edu.cs.visframework.dao.ProcessLogic;
import edu.cs.visframework.dao.SentimentLogic;
import edu.cs.visframework.dao.TwitterDataPlugin;
import edu.cs.visframework.dao.VisPlugin;
import edu.cs.visframework.dao.YtDataPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.logging.FileHandler;

/**
 * This is the global parser service for MVC pattern,
 * and it uses beanFactory pattern from Spring IOC.
 *
 * **User needs to config beanFactory.xml in order to add their implementations.**
 *
 * This class parses the input json and return the allocated data plugin / graph plugins.
 *
 * NOTE: Attached an example json we used for input.
 * new_json: {"workSpace":[{"dataSource":[{"dataSourceName":"twitter","dataSourceUrl":"SenSchumer"}],"visSource":[{"visSourceName":"heatmap"},{"visSourceName":"bar"}],"processSource":{"filter":{"startDate":"2017-01-08","endDate":"2021-11-22"}}}]}
 *
 * @author Shicheng Huang
 */
@Service
public class ParserService {
    /**
     * Referencing IOC resources.
     */
    @Resource
    private FilterLogic filterLogic;

    @Resource
    private SentimentLogic sentimentLogic;

    @Resource
    private BeanFactory xmlBeanFactory;


    /**
     * Get the count of work space.
     * @param json
     * @return
     */
    public int getWorkSpaceCount(JSONObject json) {
        int workSpaceCount;
        try {
            workSpaceCount = json.getJSONArray("workSpace").length();
        } catch (JSONException e) {
            return -1;
        }
        return workSpaceCount;
    }

    /**
     * Get the workSpace JSon from input.
     * @param json input.
     * @param i index of workSpace.
     * @return json of workSpace.
     */
    public JSONObject getWorkSpace(JSONObject json, int i) {
        JSONObject workSpace;
        try {
            JSONArray workSpaces = json.getJSONArray("workSpace");
            workSpace = workSpaces.getJSONObject(i);
        } catch (JSONException e) {
            return null;
        }
        return workSpace;
    }

    /**
     * Get data plugin Count.
     * @param workSpace workSpace.
     * @return workSpace
     */
    public int getDataPluginCount(JSONObject workSpace) {
        int dataPluginCount;
        try {
            dataPluginCount = workSpace.getJSONArray("dataSource").length();
        } catch (JSONException e) {
            return -1;
        }
        return dataPluginCount;
    }

    /**
     * Get data plugin.
     * @param workSpace workspace
     * @param i index
     * @return dataplugin
     */
    public DataPlugin getDataPlugin(JSONObject workSpace, int i) throws Exception {
        JSONArray dataSources = workSpace.getJSONArray("dataSource");
        JSONObject dataSource = dataSources.getJSONObject(i);

        return (DataPlugin) xmlBeanFactory.getBean(dataSource.getString("dataSourceName")
                    + "DataPlugin");
    }

    /**
     * Get vis plugin Count.
     * @param workSpace workSpace.
     * @return workSpace
     */
    public int getVisPluginCount(JSONObject workSpace) {
        int visPluginCount;
        try {
            visPluginCount = workSpace.getJSONArray("visSource").length();
        } catch (JSONException e) {
            return -1;
        }
        return visPluginCount;
    }

    /**
     * Get vis plugin.
     * @param workSpace workSpace.
     * @return workSpace
     */
    public VisPlugin getVisPlugin(JSONObject workSpace, int i) throws Exception {
        JSONArray dataSources = workSpace.getJSONArray("visSource");
        JSONObject dataSource = dataSources.getJSONObject(i);

        return (VisPlugin) xmlBeanFactory.getBean(dataSource.getString("visSourceName")
                + "VisPlugin");
    }

    /**
     * Get process Logic.
     * @param workSpace
     * @return
     */
    public ProcessLogic getProcessLogic(JSONObject workSpace) {
        return filterLogic;
    }

    /**
     * Get NLP logic
     * @param workSpace
     * @return
     */
    public NlpLogic getNlplogic(JSONObject workSpace) {
        return sentimentLogic;
    }

    /**
     * Get data source json.
     * @param workSpace
     * @param j
     * @return
     */
    public JSONObject getDataSource(JSONObject workSpace, int j) {
        JSONObject data = null;
        try {
            JSONArray dataSources = workSpace.getJSONArray("dataSource");
            data = dataSources.getJSONObject(j);
        } catch (JSONException e) {
            return null;
        }
        return data;
    }

    /**
     * Get vis Json.
     * @param workSpace
     * @param j
     * @return
     */
    public JSONObject getVisSource(JSONObject workSpace, int j) {
        JSONObject data = null;
        try {
            JSONArray dataSources = workSpace.getJSONArray("visSource");
            data = dataSources.getJSONObject(j);
        } catch (JSONException e) {
            return null;
        }
        return data;
    }
}
