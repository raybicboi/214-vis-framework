
import java.io.IOException;
import java.util.*;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import fi.iki.elonen.NanoHTTPD;
import framework.core.DataPlugin;
import framework.core.Framework;
import framework.gui.VisualizationPlugin;

public class App extends NanoHTTPD {

    private Framework f;
    private Template template;
    private List<DataPlugin> dataPlugins;
    private List<VisualizationPlugin> visPlugins;

    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    public App() throws IOException {
        super(8080);

        this.f = new Framework();

        // initialize the plugin lists
        dataPlugins = loadDataPlugins();
        visPlugins = loadVisPlugins();

        dataPlugins.forEach(x -> f.registerDataPlugin(x));
        visPlugins.forEach(x -> f.registerVisPlugin(x));

        Handlebars handlebars = new Handlebars();
        this.template = handlebars.compile("optionScreen");

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:8080/ \n");

    }


    @Override
    public Response serve(IHTTPSession session) {
        try {
            String uri = session.getUri();
            Map<String, String> params = session.getParms();
//            if (uri.equals("/plugin")) {
//                game.startNewGame(plugins.get(Integer.parseInt(params.get("i"))));
//            } else if (uri.equals("/play")){
//                if (game.hasGame()) {
//                    game.playMove(Integer.parseInt(params.get("x")), Integer.parseInt(params.get("y")));
//                }
//            }
            GraphDisplay screen = GraphDisplay.forPlugin(this.f);
            String HTML = this.template.apply(screen);
            return newFixedLengthResponse(HTML);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Load data plugins listed in META-INF/services/DataPlugin
     *
     * @return List of instantiated data plugins
     */
    private static List<DataPlugin> loadDataPlugins() {
        ServiceLoader<DataPlugin> dPlugins = ServiceLoader.load(DataPlugin.class);
        List<DataPlugin> result = new ArrayList<>();
        for (DataPlugin dP : dPlugins) {
            System.out.println("Loaded plugin " + dP.getPluginName());
            result.add(dP);
        }
        return result;
    }

    /**
     * Load visualization plugins listed in META-INF/services/VisualizationPlugin
     *
     * @return List of instantiated visualization plugins
     */
    private static List<VisualizationPlugin> loadVisPlugins() {
        ServiceLoader<VisualizationPlugin> vPlugins = ServiceLoader.load(VisualizationPlugin.class);
        List<VisualizationPlugin> result = new ArrayList<>();
        for (VisualizationPlugin vP : vPlugins) {
            System.out.println("Loaded plugin " + vP.getPluginName());
            result.add(vP);
        }
        return result;
    }
}