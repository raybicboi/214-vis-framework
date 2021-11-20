package framework.gui;

import framework.core.Framework;

public interface VisualizationPlugin {

    void onRegister(Framework f);

    String getPluginName();

    String getLink();
}
