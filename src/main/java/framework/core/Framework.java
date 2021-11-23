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

    /**
     * No arg constructor for the Framework
     */
    public Framework() {
        footer = DEFAULT_FOOTER;
        registeredVisPlugins = new ArrayList<VisualizationPlugin>();
        registeredDataPlugins = new ArrayList<DataPlugin>();
        activeData = new ArrayList<>();
    }

    /**
     * Imports the selected data plugin onto the Framework.
     * @param p the selected data plugin
     */
    public void registerDataPlugin(DataPlugin p) {
        String name = p.getPluginName();
        if (registeredDataPlugins.stream().
                filter(x -> x.getPluginName().equals(name)).
                collect(Collectors.toList()).isEmpty()) {
//            p.onRegister(this);
            registeredDataPlugins.add(p);
        }
    }

    /**
     * Imports the selected visualization plugin onto the Framework.
     * @param p the selected visualization plugin
     */
    public void registerVisPlugin(VisualizationPlugin p) {
        String name = p.getPluginName();
        if (registeredVisPlugins.stream().
                filter(x -> x.getPluginName().equals(name)).
                collect(Collectors.toList()).isEmpty()) {
            p.onRegister(this);
            registeredVisPlugins.add(p);
        }
    }

    /**
     * Returns a list of the names of all the data plugins.
     * @return {@code List<String}
     */
    public List<String> getRegisteredDataPluginNames() {
        return registeredDataPlugins.stream().map(x -> x.getPluginName()).
                collect(Collectors.toList());
    }

    /**
     * Returns a list of the names of all the visualization plugins.
     * @return {@code List<String}
     */
    public List<String> getRegisteredVisPluginNames() {
        return registeredVisPlugins.stream().map(x -> x.getPluginName()).
                collect(Collectors.toList());
    }

    /**
     * Selects the appropriate data plugin for later use.
     * @param p selected data plugin
     */
    public void setDataPlugin(DataPlugin p) {
        if (registeredDataPlugins.contains(p))
            currDataPlugin = p;
        registerDataPlugin(p);
        currDataPlugin = p;

    }

    /**
     * Selects the appropriate visualization plugin for later use.
     * @param p selected visualization plugin
     */
    public void setVisPlugin(VisualizationPlugin p) {
        if (registeredVisPlugins.contains(p))
            currVisPlugin = p;
        registerVisPlugin(p);
        currVisPlugin = p;
    }

    /**
     * Getter method for the name of the current data plugin
     * @return {@code String}
     */
    public String getCurrentDataPluginName() {
        return currDataPlugin.getPluginName();
    }

    /**
     * Imports the data for later use.
     */
    public void importData() {
        currDataPlugin.importData("");
        activeData = currDataPlugin.extractData().stream().collect(Collectors.toList());
    }

    /**
     * Getter method that returns a list of relevant data to be passed on to the visualization.
     * @return {@code List<Country>}
     */
    public List<Country> getActiveData() {
        return activeData;
    }

    /**
     * Returns the JS String based on the visualization plugin selected and the data imported.
     * @return {@code String}
     */
    public String getCurrentExtraJS() {
        if (activeData.isEmpty() && currDataPlugin != null)
            importData();
        if (currVisPlugin != null) {
            return currVisPlugin.getExtraJS();
        }
        return "";
    }
}
