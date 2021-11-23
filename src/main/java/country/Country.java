package country;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Country
 *
 * Each country stores numerous (numerical) data points.
 * The country also has a name and a region that it belongs to.
 */
public class Country {
    private final String name;
    private final String region;
    private Map<String, Double> data;

    /**
     * Constructor. Sets the name and region
     * @param name
     */
    public Country(String name, String region) {
        this.name = name;
        this.region = region;
    }

    /**
     * Returns a list of the fields (column names)
     * @return {@code Set<String>}
     */
    public Set<String> fieldSet() {
        return data.keySet();
    }

    /**
     * Returns the count of fields of numerical information that this country
     * is storing.
     * @return an integer
     */
    public int dataPointCount() {
        return data.size();
    }

    /**
     * Returns the {@link double} data value for the given field, or 0.0
     * if not found.
     * @param field
     * @return {@link double}
     */
    public double getDataPoint(String field) {
        try {
            return data.get(field).doubleValue();
        } catch (NullPointerException e) {
            return 0.0;
        }
    }

    /**
     * Checks whether the datapoint is null
     * @param field
     * @return boolean
     */
    public boolean hasDataPoint(String field) {
        try {
            double x =  data.get(field).doubleValue();
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Returns the name of this country.
     * @return {@code String}
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the name of this country's region.
     * @return {@code String}
     */
    public String getRegion() {
        return region;
    }

    /**
     * Sets the data for this country.
     * @param data - a String |--> Double {@code Map} where keys are the names
     *             of fields and vals are a {@code double} corresponding
     *             to that field.
     */
    public void setData(Map<String, Double> data) {
        this.data = data;
    }

    /**
     * toString for the country
     * @return {@code String}
     */
    @Override
    public String toString() {
        return "Country: " + name + " Region: " + region;
    }

    /**
     * Returns a string representation of this country's data.
     * @return {@code String}
     */
    public String printData() {
        List<Double> vals = data.values().stream().toList();
        List<String> keys = data.keySet().stream().toList();
        String pBreak = "========================";
        String res = this + "\n" + pBreak + pBreak + "\n";
        if (keys.size() != vals.size()) {
            return res;
        }

        for (int i = 0; i < keys.size(); i++) {
            String k = keys.get(i);
            Double v = vals.get(i);
            if (k != null && v != null)
                res += k + " : " + v + "\n";
        }

        return res;
    }

    /**
     * Adds a data point to the existing map.
     * @param fieldName - the string representing the name of the field
     * @param value - the {@link Double} numerical value for the given field.
     */
    public void addDataPoint(String fieldName, Double value) {
        data.put(fieldName,value);
    }
}
