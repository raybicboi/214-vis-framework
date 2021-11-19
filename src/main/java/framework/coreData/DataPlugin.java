package framework.coreData;

import country.Country;

import java.util.Date;
import java.util.Set;

public interface DataPlugin {

    void importData(String url);

    void getOptions(int[] options);

    Set<Country> extractData();
}
