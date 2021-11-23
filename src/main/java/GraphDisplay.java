//import framework.gui;

import country.Country;
import framework.core.Framework;
import framework.gui.Plugin;

import java.util.List;
import java.util.Set;

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

//    private static Dropdown[] getDropdowns(Framework f) {
//        String text = game.getSquare(x,y);
//        String link = "/play?x="+x+"&y="+y;
//        cells[width * y + x] = new Cell(text, clazz, link);
//        return cells;
//    }

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
//        return """
//                var data = [{
//                    type: 'scattergeo',
//                    mode: 'markers',
//                    locations: ['FRA', 'DEU', 'RUS', 'ESP'],
//                    marker: {
//                        size: [20, 30, 15, 10],
//                        color: [10, 20, 40, 50],
//                        cmin: 0,
//                        cmax: 50,
//                        colorscale: 'Greens',
//                        colorbar: {
//                            title: 'Some rate',
//                            ticksuffix: '%',
//                            showticksuffix: 'last'
//                        },
//                        line: {
//                            color: 'black'
//                        }
//                    },
//                    name: 'europe data'
//                }];
//
//                var layout = {
//                    'geo': {
//                        'scope': 'europe',
//                        'resolution': 50
//                    }
//                };
//
//                Plotly.newPlot('myDiv', data, layout);
//                """;
        return extraJS;
        //return "alert(\"foo\");";
    }
}