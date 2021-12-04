package edu.cs.visframework.dao;

import edu.cs.visframework.pojo.Value;

import org.json.JSONObject;

import java.io.*;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVDataPlugin implements DataPlugin {

    public static void main(String args[]) throws IOException, URISyntaxException {
        DataPlugin newsPlugin = new CSVDataPlugin();
        List returnArray = newsPlugin.download(null);
        System.out.println(returnArray.toString());
//        getRedditAuth();
    }

    /**
     * Download reddit comments based on post from the reddit API and returns
     * a list of comments.
     * @param json JSONObject from front end with twitter user name
     * @return list of </Value>
     */
    @Override
    public List<Value> download(JSONObject json) {
        List<List<String>> imported = readCSVToList(json);
        return convertCSVToValue(imported);
    }

//    private static void getRedditAuth() throws IOException {
////        String commandToAuth = "curl -X POST -A 'UserAgent' -d 'grant_type=password&username=Technical-Ice2745&password=#Raybicboi1224' --user 'hwHzyf5J_78zAsOkMHMLEQ:31E8wY_pR-gN1idy1vw_NuzWGjHsrw' https://www.reddit.com/api/v1/access_token";
//        String commandToAuth = "curl -H ‘Authorization: Bearer 482187236568-7LmS338nxnCPgHbprFhguCY78NC3dA’ -A ‘Framework6c by Technical-Ice2745’ https://oauth.reddit.com/api/v1/me?limit=1";
//        ProcessBuilder processBuilder = new ProcessBuilder(commandToAuth.split(" "));
//        processBuilder.directory(new File("src\\main\\java\\edu\\cs\\visframework"));
//        try {
//            Process process = processBuilder.start();
//            InputStream inputStream = process.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//            int exitCode = process.exitValue();
////            int exitCode = process.waitFor();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////        process.destroy();
//    }

    public static List<List<String>> readCSVToList(JSONObject config) {
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