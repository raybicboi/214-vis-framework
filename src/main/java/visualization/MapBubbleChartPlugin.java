package visualization;

import country.Country;
import framework.core.Framework;
import framework.gui.VisualizationPlugin;

import java.util.*;
import java.util.stream.Collectors;

public class MapBubbleChartPlugin  implements VisualizationPlugin {

    private static final String PLUGIN_NAME = "Map Bubble Chart Plugin Plugin";
    private static final int DEFAULT_REGION_COUNT = 5;
    private int REGION_COUNT;
    private List<Country> data;
    private List<String> dataFields;
    //maps countries to lists of accumulated values corresponding to the data fields
    private Map<String, List<Double>> cData;
    //maps countries to lists of mean accumulated values corresponding to the data fields
    private Map<String, List<Double>> mcData;
    private Framework f;


    @Override
    public void onRegister(Framework f) {
        REGION_COUNT = DEFAULT_REGION_COUNT;
        extractFromFramework(f);
        buildCategoricalData();
        this.f = f;
    }

    /**
     * Private helper method that gets the data from the framework, which is called
     * in onRegister
     */
    private void extractFromFramework(Framework f) {
        data = f.getActiveData();
    }

    /**
     * Extracts and stores the list of data fields in use by the countries
     * in the data being stored.
     */
    private void getDataFields() {
        Set<String> fields = new HashSet<>();
        for (Country c : data) {
            fields.addAll(c.fieldSet());
        }
        dataFields = fields.stream().toList();
    }

    /**
     * Gets a list of the names of all the different countries that are represented
     * @return {@code List<String>}
     */
    private List<String> getCountryNames() {
        List<String> countryNames = new ArrayList<>();
        for (Country c : data) {
            String country = c.getName();
            if (!countryNames.contains(country)) {
                countryNames.add(country);
            }
        }
        return countryNames;
    }

    /**
     * Builds the categorical data available. Sets the {@field cData} to
     * a map where keys are country names and values are lists of doubles,
     * accumulated across the fields for countries belonging to this country.
     * Sets {@field mcData} to the same, expect that values are averaged
     * across country.
     */
    private void buildCategoricalData() {
        getDataFields();
        Map<String, List<Double>> dataByCat = new HashMap<>();
        Map<String, List<Double>> meanDataByCat = new HashMap<>();
        List<String> countryNames = getCountryNames();
        //computing data points (both sum and mean) across countries
        for (String cName : countryNames) {
//            List<String> allCountries = loadAllCountries();
            if (null == country2Code(cName)) continue;
//            if (!allCountries.contains(cName)) continue;
            List<Double> accumVals = new ArrayList<>();
            List<Integer> countPerFieldInCountry = new ArrayList<>();
            //init
            for (int i = 0; i < dataFields.size(); i++) {
                accumVals.add(0.0);
                countPerFieldInCountry.add(0);
            }
            for (Country c : data) {
                if (c.getName().equals(cName)) {
                    for (int i = 0; i < dataFields.size(); i++) {
                        double d = accumVals.get(i);
                        double n = c.getDataPoint(dataFields.get(i));
                        accumVals.set(i,d + n);
                        int count = countPerFieldInCountry.get(i);
                        int m = c.hasDataPoint(dataFields.get(i)) ? count + 1 : count;
                        countPerFieldInCountry.set(i,m);
                    }
                }
            }
            dataByCat.put(cName, accumVals);

            List<Double> meanAccumVals = new ArrayList<>();
            for (int i = 0; i < accumVals.size(); i++) {
                meanAccumVals.add(accumVals.get(i) / countPerFieldInCountry.get(i));
            }
            meanDataByCat.put(cName, meanAccumVals);
        }
        cData = dataByCat;
        mcData = meanDataByCat;
    }

    @Override
    public String getPluginName() {
        return PLUGIN_NAME;
    }

    /**
     * Helper function that assists in building the final data format, based on whether we
     * want to visualize values or mean of values.
     * @param field the column from the dataset chosen to measure
     * @param mean a flag of whether to use mean values
     * @return {@code Map<String, Double>}
     */
    private Map<String, Double> forChart(String field, boolean mean) {
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
        return trimToRegionCount(res,false);
    }

    /**
     * Helper function that limits the inputs used for the visualization based on a
     * specific target.
     * @param res the entire dataset passed into the visualization plugin
     * @param max the number of rows of data points we want
     * @return {@code Map<String, Double>}
     */
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
    public String toString() {
        return PLUGIN_NAME;
    }

    /**
     * Getter method for max number of regions used for the visualization
     * @return {@code int}
     */
    public int getRegionCount() {
        return REGION_COUNT;
    }

    /**
     * Setter method for providing the max number of regions used for the visualization
     * @param REGION_COUNT the max number of regions
     */
    public void setRegionCount(int REGION_COUNT) {
        this.REGION_COUNT = REGION_COUNT;
    }

    @Override
    public String getExtraJS() {
        if (data.isEmpty())
            extractFromFramework(f);
        Map<String, Double> forChart = forChart("population",false);
        return getJS(forChart);
    }

    /**
     * Private helper method that gets the JS String for the graph to be passed into plot.ly
     * @param data the final cleaned data set to pass in
     * @return {@code String}
     */
    private String getJS(Map<String, Double> data) {
        String d = """
            var data = [{
                type: 'scattergeo',
                mode: 'markers',\n
                """;
        List<String> keys = data.keySet().stream().toList();
        List<Double> vals = new ArrayList<>();
        List<Double> vals2 = new ArrayList<>();
        String location = "locations: [";
        for (String k : keys) {
            String code = country2Code(k);
            String iso3;
            if (code != null) {
                Locale locale = new Locale("en", code);
                iso3 = locale.getISO3Country();
            } else {
                iso3 = "";
            }
            vals.add(data.get(k));
            location += addApos(iso3) + ",";
        }
        for (String k : keys) {
            Double prop = proportion(vals, data.get(k));
            if (prop > 0.0) vals2.add(prop);
            else vals2.add(0.0);
        }
        location += "],\n";
        String marker = "marker : {\n";
        String values = "size: " +  vals2 +",\n";
        String color = """
                cmin: 0,
                cmax: 30,
                line: {
                    color: 'black'
                }
            },
        }];\n""";
        String layout = """
            var layout = {
                'geo': {
                    'scope': 'world',
                    'resolution': 100
                }
            };\n""";
        String plotly = "\n Plotly.newPlot('myDiv', data, layout);";
        return d + location + marker + values + color + layout + plotly;
    }

    private String addApos(String x) {
        return "'" + x + "'";
    }

    /**
     * Private helper method converts a string value of a country into its ISO2 code
     * @param country the name of the country
     * @return {@code String}
     */
    private String country2Code(String country) {
        Map<String, String> countries = new HashMap<>();
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("en", iso);
            countries.put(l.getDisplayCountry(), iso);
        }
        return countries.get(country);
    }

    /**
     * Private helper method that determines the size of the bubble to be displayed.
     * @param list the entire list of double values
     * @param d the specific value in the list being compared
     * @return {@code Double}
     */
    private Double proportion(List<Double> list, Double d) {
        Double total = 0.0;
        for (Double dou : list) {
            total += dou;
        }
        return d * Double.valueOf(DEFAULT_REGION_COUNT) * 40 / total;
    }

    /**
     * Uses the Locale package in Java to return a String list of all countries.
     * @return {@code List<String>}
     */
    private List<String> loadAllCountries() {
        ArrayList<String> result = new ArrayList<String>();
        for (Locale locale : Locale.getAvailableLocales())
        {
            result.add(locale.getDisplayCountry());
        }
        return result;
    }

    @Override
    public void resetData() {
        data = new ArrayList<>();
        dataFields = new ArrayList<>();
        cData = new HashMap<>();
        mcData = new HashMap<>();
    }
}
