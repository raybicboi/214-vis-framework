package data;

import country.Country;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ApiPluginTest {
    private static final int NUM_DATA_POINTS = 231;
    Country japan;
    ApiPlugin a;

    @Before
    public void setUp() {
        a = new ApiPlugin();
        a.importData("https://api.api-ninjas.com/v1/" +
                "country?min_gdp=1");
        japan = a.extractData().stream().filter
                        (x -> x.getName().equals("Japan"))
                .collect(Collectors.toList()).get(0);
    }

    /**
     * Test that the correct amount of countries get extracted from the data.
     */
    @Test
    public void testExtractDataSize() {
        Set<Country> countries = a.extractData();
        System.out.println(countries);
        assertEquals(NUM_DATA_POINTS, countries.size());
    }

    /**
     * Tests that the countries were made, choosing Japan arbitrarily
     * as the country to check for existence.
     */
    @Test
    public void testExtractDataNotNullCountry() {
        assertNotNull(japan);
    }

    /**
     * Checks that there are some data fields stored within the country after
     * extracting data through the API.
     */
    @Test
    public void testExtractDataFieldsAcquired() {
        assertTrue(japan.dataPointCount() > 10);
    }

    @Test
    public void testCorrectRegion() {
        assertEquals("Eastern Asia", japan.getRegion());
    }

    @Test
    public void printData() {
        Set<Country> r = a.extractData();
        r.forEach(x -> System.out.println(x.printData()));
    }
}


















