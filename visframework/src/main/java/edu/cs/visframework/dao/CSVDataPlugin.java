package edu.cs.visframework.dao;

import edu.cs.visframework.pojo.Value;
import org.json.JSONObject;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The twitter plugin takes in a csv file in the visframework directory of texts and timestamps
 * and returns a list of </Value>
 */
public class CSVDataPlugin implements DataPlugin {

    public static void main(String args[]) throws IOException, URISyntaxException {
        DataPlugin newsPlugin = new CSVDataPlugin();
        List returnArray = newsPlugin.download(null);
        System.out.println(returnArray.toString());
    }

    /**
     * 'Download' csv file and converts the contents into List<Value> format
     * @param json JSONObject from front end that is the name of the csv file
     * @return list of </Value>
     */
    @Override
    public List<Value> download(JSONObject json) {
        List<List<String>> imported = readCSVToList(json);
        return convertCSVToValue(imported);
    }

    /**
     * Reads csv file and converts content into a 2D array
     * @param config JSONObject from front end that is the name of the csv file
     * @return List\<List<String>>> 2D array with each inner array as a row of datapoint
     */
    private static List<List<String>> readCSVToList(JSONObject config) {
        List<List<String>> result = new ArrayList<>();
        try {
            String filePath = config.getString("dataSourceUrl");
            String windowsPath = "src\\main\\java\\edu\\cs\\visframework\\";
//            String macPath = "src/main/java/edu/cs/visframework";
            String path = windowsPath + filePath;

            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                reader.readLine();
                String row;
                while ((row = reader.readLine()) != null) {
                    String[] values = row.split(",");
                    result.add(Arrays.asList(values));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Reads in the converted csv contents and converts to common data format for the framework
     * @param input the 2d list of converted csv contents
     * @return List\<Value> to be put into the framework
     */
    private List<Value> convertCSVToValue(List<List<String>> input) {
        List<Value> result = new ArrayList<>();
        for (List<String> subList : input) {
            if (subList.size() == 2) {
                System.out.println(subList.get(0));
                System.out.println(subList.get(1));
                String text = subList.get(0);
                String time = subList.get(1);
                double score = 2.0;
                Value v = new Value(time, text, score);
                result.add(v);
            }
        }
        return result;
    }

}