package visualization;

import country.Country;
import framework.core.Framework;
import framework.gui.VisualizationPlugin;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RegionalBarChartPlugin implements VisualizationPlugin {


    private static final String PLUGIN_NAME = "Regional Bar Chart Plugin";
    private static final int DEFAULT_REGION_COUNT = 5;
    private int REGION_COUNT;
    private List<Country> data;
    private List<String> dataFields;
    //maps regions to lists of accumulated values corresponding to the data fields
    private Map<String, List<Double>> cData;
    //maps regions to lists of mean accumulated values corresponding to the data fields
    private Map<String, List<Double>> mcData;
    private Framework f;


    @Override
    public void onRegister(Framework f) {
        REGION_COUNT = DEFAULT_REGION_COUNT;
        extractFromFramework(f);
        buildCategoricalData();
        this.f = f;
    }

    public void extractFromFramework(Framework f) {
        data = f.getActiveData();
    }

    /**
     * Extracts and stores the list of data fields in use by the countrys
     * in the data being stored.
     */
    public void getDataFields() {
        Set<String> fields = new HashSet<>();
        for (Country c : data) {
            fields.addAll(c.fieldSet());
        }
        dataFields = fields.stream().toList();
    }

    /**
     * Gets a list of the names of all the different regions that are represented
     * in the set of countries.
     * @return {@code List<String>}
     */
    public List<String> getRegionNames() {
        List<String> regionNames = new ArrayList<>();
        for (Country c : data) {
            String region = c.getRegion();
            if (!regionNames.contains(region)) {
                regionNames.add(region);
            }
        }
        return regionNames;
    }

    /**
     * Builds the categorical data available. Sets the {@field cData} to
     * a map where keys are region names and values are lists of doubles,
     * accumulated across the fields for countries belonging to this region.
     * Sets {@field mcData} to the same, expect that values are averaged
     * across regions.
     */
    private void buildCategoricalData() {
        getDataFields();
        Map<String, List<Double>> dataByCat = new HashMap<>();
        Map<String, List<Double>> meanDataByCat = new HashMap<>();
        List<String> regionNames = getRegionNames();
        //computing data points (both sum and mean) across regions
        for (String rName : regionNames) {
            List<Double> accumVals = new ArrayList<>();
            List<Integer> countPerFieldInRegion = new ArrayList<>();
            //init
            for (int i = 0; i < dataFields.size(); i++) {
                accumVals.add(0.0);
                countPerFieldInRegion.add(0);
            }
            for (Country c : data) {
                if (c.getRegion().equals(rName)) {
                    for (int i = 0; i < dataFields.size(); i++) {
                        double d = accumVals.get(i);
                        double n = c.getDataPoint(dataFields.get(i));
                        accumVals.set(i,d + n);
                        int count = countPerFieldInRegion.get(i);
                        int m = c.hasDataPoint(dataFields.get(i)) ? count + 1 : count;
                        countPerFieldInRegion.set(i,m);
                    }
                }
            }
            dataByCat.put(rName, accumVals);

            List<Double> meanAccumVals = new ArrayList<>();
            for (int i = 0; i < accumVals.size(); i++) {
                meanAccumVals.add(accumVals.get(i) / countPerFieldInRegion.get(i));
            }
            meanDataByCat.put(rName, meanAccumVals);
        }
        cData = dataByCat;
        mcData = meanDataByCat;
    }

    @Override
    public String getPluginName() {
        return PLUGIN_NAME;
    }

    public Map<String, Double> forChart(String field, boolean mean) {
        if (dataFields.isEmpty())
            getDataFields();
        if (mcData.isEmpty() && cData.isEmpty())
            buildCategoricalData();
        Map<String, Double> res = new HashMap<>();
        Map<String, List<Double>> data = mean ? mcData : cData;
        if (!dataFields.contains(field))
            return res;
        int loc = dataFields.indexOf(field);
        Set<String> regions = data.keySet();
        for (String r : regions) {
            res.put(r, data.get(r).get(loc));
        }
       // System .out.println("hello " + res);

        return trimToRegionCount(res,false);
    }

    private Map<String, Double> trimToRegionCount(Map<String, Double> res, boolean max) {
        List<Map.Entry<String, Double>> list = new ArrayList<>(res.entrySet());
        list.sort(Map.Entry.comparingByValue());
        List<Map.Entry<String,Double>> sub;
        if (!max) {
            sub = list.subList(0, REGION_COUNT);

        } else {
            sub = list.subList(list.size() - REGION_COUNT, list.size());
        }
        Map<String, Double> map =
                sub.stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
        return map;
    }

    @Override
    public void getOptions(int[] options) {}

    @Override
    public String toString() {
        return PLUGIN_NAME;
    }

    public int getRegionCount() {
        return REGION_COUNT;
    }

    public void setRegionCount(int REGION_COUNT) {
        this.REGION_COUNT = REGION_COUNT;
    }

    @Override
    public String getLink() {
        return null;
    }

    @Override
    public String getExtraJS() {
        if (data.isEmpty())
            extractFromFramework(f);
        Map<String, Double> forChart = forChart("population",false);
        //System.out.println(getJS(forChart));
        return getJS(forChart);
    }

    public String getJS(Map<String, Double> data) {
        String result = "var data = [{\n";
        result += "type: 'bar',\n";
        List<String> keys = data.keySet().stream().toList();
        List<Double> vals = new ArrayList<>();
        String Y = "y: [";
        for (String k : keys) {
            vals.add(data.get(k));
            Y += addApos(k) + ",";
        }
        String X = "x: " + vals + ",\n";
        Y += "],\n";
        String orientation = "orientation: 'h'\n" + "}];";
        String plotly = "\n Plotly.newPlot('myDiv', data);";
        //System.out.println(result + X + Y + orientation + plotly);
        return result + X + Y + orientation + plotly;
    }

    private String addApos(String x) {
        return "'" + x + "'";
    }

    @Override
    public void resetData() {
        data = new ArrayList<>();
        dataFields = new ArrayList<>();
        cData = new HashMap<>();
        mcData = new HashMap<>();
    }
}
