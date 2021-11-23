import country.Country;
import framework.core.Framework;
import framework.gui.Plugin;

import java.util.List;

public class GraphDisplay {

    private final Plugin[] dataPlugins;
    private final Plugin[] visPlugins;
    private final List<Country> activeData;
    private final String extraJS;

    public GraphDisplay(Plugin[] dp, Plugin[] vp, List<Country> activeData, String extraJS) {
        this.dataPlugins = dp;
        this.visPlugins = vp;
        this.activeData = activeData;
        this.extraJS = extraJS;
    }

    public static GraphDisplay forPlugin(Framework f) {
        Plugin[] data = getDataPlugins(f);
        Plugin[] vis = getVisPlugins(f);
        List<Country> activeData = f.getActiveData();
        return new GraphDisplay(data, vis, activeData, f.getCurrentExtraJS());
    }

    private static Plugin[] getDataPlugins(Framework f) {
        List<String> dataPlugins = f.getRegisteredDataPluginNames();
        Plugin[] plugins = new Plugin[dataPlugins.size()];
        for (int i = 0; i < dataPlugins.size(); i++){
            String link = "/dat_plugin?i="+ i;
            plugins[i] = new Plugin(dataPlugins.get(i), link);
        }
        return plugins;
    }

    private static Plugin[] getVisPlugins(Framework f) {
        List<String> visPlugins = f.getRegisteredVisPluginNames();
        Plugin[] plugins = new Plugin[visPlugins.size()];for (int i = 0; i < visPlugins.size(); i++){
            String link = "/vis_plugin?i="+ i;
            plugins[i] = new Plugin(visPlugins.get(i), link);
        }
        return plugins;
    }

    public String getName() {
        return "Country Data Visualizer";
    }

    public Plugin[] getDataPlugins() {
        return this.dataPlugins;
    }

    public Plugin[] getVisPlugins() {
        return this.visPlugins;
    }

    public String getExtraJS() {
        return extraJS;
    }
}