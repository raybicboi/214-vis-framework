package visualization;

import framework.core.Framework;
import framework.gui.VisualizationPlugin;

public class BubbleChartPlugin implements VisualizationPlugin {

    private static final String PLUGIN_NAME = "Bubble Chart Plugin";

    @Override
    public void onRegister(Framework f) {
        System.out.println("tmp");
    }

    @Override
    public void resetData() {

    }

    @Override
    public String getExtraJS() {return "";}
    @Override
    public String getPluginName() {
        return PLUGIN_NAME;
    }

    @Override
    public String getLink() {
        return null;
    }

    @Override
    public void getOptions(int[] options) {
        System.out.println("tmp");
    }

    @Override
    public String toString() {
        return PLUGIN_NAME;
    }
}
