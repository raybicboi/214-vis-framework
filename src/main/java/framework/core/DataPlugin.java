package framework.core;

import country.Country;

import java.util.Set;

public interface DataPlugin {

    void onRegister(Framework f);

    void importData(String source);


    Set<Country> extractData();

    String getPluginName();

    void begin();
}
