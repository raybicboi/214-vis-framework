package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CsvPlugin {

    public void readCSV(String csvFile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
