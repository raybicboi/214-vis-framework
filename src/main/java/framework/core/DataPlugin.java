package framework.core;

import country.Country;

import java.util.Set;

public interface DataPlugin {

    /**
     * Return the name of this plugin.
     * @return {@link String}
     */
    String getPluginName();

    /**
     * Given the name from the CSV file, imports the data and stores it as
     * a set of countries in the private field that this class stores
     * (countries) Set.
     * @param source - either an API url or csv directory string
     */
    void importData(String source);

    /**
     * Returns a set of all the {@link Country} that were acquired from the
     * API and built to include all numerical data.
     * @return {@link Set} of the countries.
     */
    Set<Country> extractData();
}
