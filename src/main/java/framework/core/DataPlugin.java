package framework.coreData;

import country.Country;

import java.util.Set;

public interface DataPlugin {

    void importData(String source);


    Set<Country> extractData();
}
