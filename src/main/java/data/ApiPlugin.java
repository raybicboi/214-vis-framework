package data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import country.Country;
import framework.core.DataPlugin;
import framework.core.Framework;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class ApiPlugin implements DataPlugin {

    private Set<Country> countries;

    /**
     * Current API key to interact with this API:
     *  https://api-ninjas.com/api/country
     */
    private static final String apiKey = "hU1pYu1g4/nxVl8vV97lsg==" +
                                            "34WyNCC3ildfEQHe";
    /**
     * Query URL to extract all data
     */
    private static final String apiUrl = "https://api.api-ninjas.com/v1/" +
                                            "country?min_gdp=1";

    private static final String API_COUNTRY_DATA_PLUGIN =
            "API Country Data Plugin";

    /**
     * Return the name of this plugin.
     * @return {@link String}
     */
    public String getPluginName() {
        return API_COUNTRY_DATA_PLUGIN;
    }

    /**
     * Returns the Json node that was returned as a response from the API
     * when querying. This contains all the information for extraction,
     * but can be null if there was an error.
     * @param apiUrl
     * @return {@link JsonNode} with data to be extracted.
     */
    private static JsonNode getAllData(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection)
                                                url.openConnection();
            connection.setRequestProperty("X-Api-Key", apiKey);
            InputStream responseStream = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(responseStream);


        } catch (Exception e) {
            //TODO: figure out error handling
            System.out.println("dang, we had an error. What should we do here?");
        }

        return null;
    }

    //TODO: figure out extracting partial data

    /**
     * Builds a set of all the countries from the given jsonNode.
     * @param jsonNode
     * @return a set of all the countries extracted from the JsonNode.
     */
    private static Set<Country> buildCountries(JsonNode jsonNode) {
        Set<Country> res = new HashSet<>();
        if (jsonNode == null || !jsonNode.isArray()) {
            return res;
        }

        ArrayNode arrayNode = (ArrayNode) jsonNode;
        int num_countries = arrayNode.size();

        //builds each country one by one
        for (int i = 0; i < num_countries; i++) {
            Map<String, Double> countryDataSet = new HashMap<>();
            ObjectNode col = (ObjectNode) arrayNode.get(i);
            Iterator<Map.Entry<String, JsonNode>> fields = col.fields();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> next = fields.next();
                JsonNode val = next.getValue();
                String key = next.getKey();
                //only add numerical data to the data set
                if (val instanceof DoubleNode) {
                    countryDataSet.put(key,val.doubleValue());
                }
            }

            String n = "name";
            String r = "region";
            String e = "";
            //Sets empty string as default vals for name/region if absent
            Country c = new Country(col.get(n).asText(e),col.get(r).asText(e));
            c.setData(countryDataSet);
            res.add(c);
        }

        return res;
    }

    public static void main(String[] args) {
        //extractDataStaticForTesting();
    }

    @Override
    public void onRegister(Framework f) {

    }

    @Override
    public void importData(String url) {
        JsonNode node = getAllData(apiUrl);
        countries = buildCountries(node);
    }

    /**
     * Returns a set of all the {@link Country} that were acquired from the
     * API and built to include all numerical data.
     * @return {@link Set} of the countries.
     */
    @Override
    public Set<Country> extractData() {
        return countries;
    }

    @Override
    public void begin() {}

}