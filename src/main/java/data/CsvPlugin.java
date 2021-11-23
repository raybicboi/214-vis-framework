package data;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import country.Country;
import framework.core.DataPlugin;
import framework.core.Framework;

import java.io.FileReader;
import java.io.IOException;

import java.util.*;

public class CsvPlugin implements DataPlugin {

    private Set<Country> countries;

    private static final String CSV_PLUGIN =
            "CSV File Data Plugin";
    /**
     * Given the name of a CSV file, reads it into a list of string arrays,
     * where each string array represents a row of the file.
     * @param csvFile
     * @return {@link List<String[]>} representation of the csv file.
     */
    public static List<String[]> readCSVToList(String csvFile) {
        try {
            //String windowsPath = "src\\main\\java\\data\\";
            String macPath = "src/main/java/data/";
            String path = macPath + csvFile;

            try (CSVReader reader = new CSVReader(new FileReader(path))) {
                List<String[]> r = reader.readAll();
                return r;
            } catch (CsvException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Intermediate step in data conversion. Takes a list of string arrays and
     * converts it to an equivalent list of lists of strings.
     * @param intermediate
     * @return {@link List<List<String>>} where the outer each inner list is
     * a row from the csv file, including the header row.
     */
    private static List<List<String>> convertDataIntermediate(List<String[]>
                                                               intermediate) {
        List<List<String>> res = new ArrayList<>();
        for (int i = 0; i < intermediate.size(); i++) {
            List<String> innerOld = Arrays.stream(intermediate.get(i)).toList();
            res.add(innerOld);
        }

        return copyList(res);
    }

    /**
     * Returns a deep copy of the given nested list.
     * @param res
     * @return a copy of {@param res} that won't be affected by changes
     * to {@param res}
     */
    private static List<List<String>> copyList(List<List<String>> res) {
        List<List<String>> resCopy = new ArrayList<>();
        for (int i = 0; i < res.size(); i++) {
            List<String> inner = new ArrayList<>();
            for (int j = 0; j < res.get(i).size(); j++) {
                inner.add(res.get(i).get(j));
            }
            resCopy.add(inner);
        }
        return resCopy;
    }

    /**
     * Given the nested list representing data from the CSV file and an
     * index, extracts the values in the zth column of the dataset, excluding
     * the header.
     * @param data - nested list representing data
     * @param z - the column number to acquire values from, 0-indexed.
     * @return a list of strings, which are the values from the given column.
     */
    private static List<String> getNames(List<List<String>> data, int z) {
        List<String> res = new ArrayList<>();
        if (z >= data.size() || z < 0)
            return res;
        for (int i = 1; i < data.size(); i++) {
            res.add(data.get(i).get(z));
        }
        return res;
    }

    /**
     * Given the data points for a country, a list of fields which includes
     * all the fields, a name and a region, builds the country.
     * @param name
     * @param region
     * @param cData - ordered list of strings corresponding to data points
     * @param fields - ordered list of strings corresponding to field names
     *                  (same order as {@param cData})
     * @return {@link Country} built from the given information.
     */
    private static Country buildCountry(String name, String region,
                                      List<String> cData, List<String> fields) {

            Map<String, Double> dMap = new HashMap<>();
            //adds numerical data points to the map along with field name
            for (int j = 0; j < fields.size(); j++) {
                try {
                    dMap.put(fields.get(j), Double.parseDouble(cData.get(j)));
                } catch (NumberFormatException e) {}
                  catch (IndexOutOfBoundsException e) {}
            } //adds nothing to map if data is not a double
            Country c = new Country(name, region);
            c.setData(dMap);
            return c;
    }

    /**
     * Builds all the countries given the nested list representation of the
     * CSV file.
     * @param data
     * @return {@link Set} of the countries.
     */
    private static Set<Country> buildCountries(List<List<String>> data) {
        Set<Country> countries = new HashSet<>();

        if (data.isEmpty())
            return countries;

        List<String> fields = data.get(0); //TODO: error handling
        List<String> countryNames = getNames(data,0);
        List<String> regionNames = getNames(data,1);

        // i starts at 1 to exclude the row of headers
        for (int i = 1; i < data.size(); i++) {
            String name = countryNames.get(i - 1);
            String region = regionNames.get(i - 1);
            countries.add(buildCountry(name,region, data.get(i), fields));
        }
        return countries;
    }


    @Override
    public void onRegister(Framework f) {

    }

    /**
     * Given the name from the CSV file, imports the data and stores it as
     * a set of countries in the private field that this class stores
     * (countries) Set.
     * @param csvFile - e.g. "myFile.csv"
     */
    @Override
    public void importData(String csvFile) {
        List<String[]> resIntermediate = readCSVToList("testLarger.csv");
        List<List<String>> data = convertDataIntermediate(resIntermediate);
        countries = buildCountries(data);
        //countries.forEach(x -> System.out.println(x.printData()));
    }

    /**
     * Extracts the country information in the form of a set.
     * @return the countries, assuming the data has already been imported.
     */
    @Override
    public Set<Country> extractData() {
        return countries;
    }

    @Override
    public String getPluginName() {
        return CSV_PLUGIN;
    }

    @Override
    public void begin() { }
}