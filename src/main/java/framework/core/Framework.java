package framework.core;

import framework.gui.VisualizationPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Framework {

    private final String NO_PLUGIN_NAME = "A Framework for Country Data";
    private final String DEFAULT_FOOTER = "No selected plugins";
    private DataPlugin currDataPlugin;
    private VisualizationPlugin currVisPlugin;
    private List<DataPlugin> registeredDataPlugins;
    private List<VisualizationPlugin> registeredVisPlugins;
    private String footer;

    public Framework() {
        footer = DEFAULT_FOOTER;
        registeredVisPlugins = new ArrayList<VisualizationPlugin>();
        registeredDataPlugins = new ArrayList<DataPlugin>();
    }

    public void registerDataPlugin(DataPlugin p) {
        p.onRegister(this);
        registeredDataPlugins.add(p);
    }

    public void registerVisPlugin(VisualizationPlugin p) {
        p.onRegister(this);
        registeredVisPlugins.add(p);
    }

    public List<String> getRegisteredDataPluginNames(){
        return registeredDataPlugins.stream().map(x -> x.getPluginName()).
                                                collect(Collectors.toList());
    }

    public List<String> getRegisteredVisPluginNames(){
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
        if (registeredDataPlugins.contains(p))
            currVisPlugin = p;
        registerVisPlugin(p);
        currVisPlugin = p;
    }
}
