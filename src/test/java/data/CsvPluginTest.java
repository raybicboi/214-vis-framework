package data;

import junit.framework.TestCase;

public class CsvPluginTest extends TestCase {

    public void testImportData() {
        CsvPlugin csvPlugin = new CsvPlugin();
        csvPlugin.importData("test.csv");
    }
}