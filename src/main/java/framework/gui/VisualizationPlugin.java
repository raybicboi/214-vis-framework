package framework.gui;


import framework.core.Framework;

public interface VisualizationPlugin {

    /**
     * Extracts the centralized data format from the framework cleans the data in preparation
     * for the visualization process
     * @param f the framework object to register from
     */
    void onRegister(Framework f);

    /**
     * Returns the name of the plugin in a readable format.
     * @return {@code String}
     */
    String getPluginName();

    /**
     * Extracts the JS String needed in order to call plot.ly
     * @return {@code String}
     */
    String getExtraJS();

    /**
     * Clears the loaded data before the plugin options are changed.
     */
    void resetData();

}
