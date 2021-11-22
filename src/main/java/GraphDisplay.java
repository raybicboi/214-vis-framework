package framework.gui;

import framework.core.Framework;

import java.util.List;

public class GraphDisplay {

    private final Plugin[] dataPlugins;
    private final Plugin[] visPlugins;

    public GraphDisplay(Plugin[] dp, Plugin[] vp) {
        this.dataPlugins = dp;
        this.visPlugins = vp;
    }

    public static GraphDisplay forPlugin(Framework f) {
        Plugin[] data = getDataPlugins(f);
        Plugin[] vis = getVisPlugins(f);
        return new GraphDisplay(data, vis);
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
        Plugin[] plugins = new Plugin[visPlugins.size()];
        for (int i = 0; i < visPlugins.size(); i++){
            String link = "/vis_plugin?i="+ i;
            plugins[i] = new Plugin(visPlugins.get(i), link);
        }
        return plugins;
    }

//    private static Dropdown[] getDropdowns(Framework f) {
//        String text = game.getSquare(x,y);
//        String link = "/play?x="+x+"&y="+y;
//        cells[width * y + x] = new Cell(text, clazz, link);
//        return cells;
//    }

    public Plugin[] getDataPlugins() {
        return this.dataPlugins;
    }

    public Plugin[] getVisPlugins() {
        return this.visPlugins;
    }
}