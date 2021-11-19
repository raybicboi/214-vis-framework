package data;

import country.Country;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.Set;

public class CsvPluginTest {

    @Test
    public void printData() {
        CsvPlugin csvPlugin = new CsvPlugin();
        csvPlugin.importData("test.csv");
        Set<Country> res = csvPlugin.extractData();
        res.forEach(x -> System.out.println(x.printData()));
    }
}