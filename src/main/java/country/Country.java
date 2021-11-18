package src.main.country;

import java.time.Year;
import java.util.List;
import java.util.Map;

public class Country {
    private final String name;
    private final int id;
    private final String continent;
    private Map<Year, List<Integer>> data;

    /**
     * Constructor. Sets the name and the country ID.
     * @param name
     * @param id
     */
    public Country(String name, int id, String continent) {
        this.name = name;
        this.id = id;
        this.continent = continent;
    }

    /**
     * Returns the name of this country.
     * @return {@code String}
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the name of this country's continent.
     * @return {@code String}
     */
    public String getContinent() {
        return continent;
    }

    /**
     * Sets the data for this country. Each key is an ordered list of
     * data points corresponding to a predetermined set of parameters.
     * @param data
     */
    public void setData(Map<Year, List<Integer>> data) {
        this.data = data;
    }

}
