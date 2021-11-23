package framework.core;

import country.Country;
import framework.gui.VisualizationPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Framework {

    private final String NO_PLUGIN_NAME = "A Framework for Country Data";
    private final String DEFAULT_FOOTER = "No selected plugins";
    private DataPlugin currDataPlugin;
    private VisualizationPlugin currVisPlugin;
    private List<DataPlugin> registeredDataPlugins;
    private List<VisualizationPlugin> registeredVisPlugins;
    private String footer;
    private List<Country> activeData;

    public Framework() {
        footer = DEFAULT_FOOTER;
        registeredVisPlugins = new ArrayList<VisualizationPlugin>();
        registeredDataPlugins = new ArrayList<DataPlugin>();
        activeData = new ArrayList<>();
    }

    public void registerDataPlugin(DataPlugin p) {
        String name = p.getPluginName();
        if (registeredDataPlugins.stream().
                filter(x -> x.getPluginName().equals(name)).
                collect(Collectors.toList()).isEmpty()) {
//            p.onRegister(this);
            registeredDataPlugins.add(p);
        }
    }

    public void registerVisPlugin(VisualizationPlugin p) {
        String name = p.getPluginName();
        if (registeredVisPlugins.stream().
                filter(x -> x.getPluginName().equals(name)).
                collect(Collectors.toList()).isEmpty()) {
            p.onRegister(this);
            registeredVisPlugins.add(p);
        }
    }

    public List<String> getRegisteredDataPluginNames() {
        return registeredDataPlugins.stream().map(x -> x.getPluginName()).
                collect(Collectors.toList());
    }

    public List<String> getRegisteredVisPluginNames() {
        return registeredVisPlugins.stream().map(x -> x.getPluginName()).
                collect(Collectors.toList());
    }

    public void setDataPlugin(DataPlugin p) {
        if (registeredDataPlugins.contains(p))
            currDataPlugin = p;
        registerDataPlugin(p);
        currDataPlugin = p;

    }

    public void setVisPlugin(VisualizationPlugin p) {
        if (registeredVisPlugins.contains(p))
            currVisPlugin = p;
        registerVisPlugin(p);
        currVisPlugin = p;
    }

    public String getCurrentDataPluginName() {
        return currDataPlugin.getPluginName();
    }

    public void importData() {
        currDataPlugin.importData("");
        activeData = currDataPlugin.extractData().stream().collect(Collectors.toList());
       // System.out.println(activeData);
    }

    public List<Country> getActiveData() {
        return activeData;
    }

    public String getCurrentExtraJS() {
        if (activeData.isEmpty() && currDataPlugin != null)
            importData();
        if (currVisPlugin != null) {
            return currVisPlugin.getExtraJS();
        }
        return "";
    }
}
