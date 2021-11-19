package country;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


import java.util.HashMap;
import java.util.Map;

public class CountryTest {
    Country russia;
    Map<String,Double> data;
    private static final double DELTA = 0.0001;

    @Before
    public void setUp() {
        russia = new Country("Russia","Asia-Europe");
        data = new HashMap();
        data.put("population",144000000.0);
        data.put("bordering countries",14.0);
        data.put("continents in",3.0);
        data.put("letters in name",6.0);
        russia.setData(data);
    }

    /**
     * Tests that the country has the expected amount of data fields after
     * the data has been set.
     */
    @Test
    public void testSetData() {
        assertEquals(4,russia.dataPointCount());
    }


    /**
     * Test that the country returns the correct value when queried for an
     * existing field.
     */
    @Test
    public void testGetDataPointExisting() {
        double lettersInName = russia.getDataPoint("letters in name");
        assertEquals(6.0, lettersInName, DELTA);
    }

    /**
     * Test that the country returns 0.0 when queried for a non-
     * existing field.
     */
    @Test
    public void testGetDataPointNotExisting() {
        double lettersInName = russia.getDataPoint("not in set");
        assertEquals(0.0,lettersInName,DELTA);
    }

    /**
     * Test that the country's data set has another field after adding one.
     */
    @Test
    public void testAddDataPoint() {
        russia.addDataPoint("testDP", 19.0);
        assertEquals(5,russia.dataPointCount());
    }


    /**
     * Test that if "x" |-> y is a key value pair and you call
     * getDataPoint with ("x", z) that the value for "x" is overriden to z.
     * Also tests that this doesn't change the size of the number of fields.
     */
    @Test
    public void testAddDataPointConflict() {
        russia.addDataPoint("continents in", 4.0);
        double continents = russia.getDataPoint("continents in");
        assertEquals(4.0,continents,DELTA);
        assertEquals(4,russia.dataPointCount());
    }
}