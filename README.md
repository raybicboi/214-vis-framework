# hw6-analytics-framework-teamdk

### To build the project
Edit the configurations to use the App method in App.java

Edit the source files to the following:

    src: src/main/java

    resources: src/main/java/resources

Run natively in IDE, since mvn exec:exec will through an "executable not found" error.

### Domain
Our project domain is comparing statistics across countries with common
statistics (ie: population, gdp, mortality_rate, etc).

### Data Plugins
Currently, we have implemented two data plugins. The first extracts data
the API ninjas resources for getting country data here: https://api-ninjas.com/api/country.
The API returns data in JSON format. The second data plugin is a general plugin for CSV files- 
but the CSV files must be formatted in a certain way to work.

    file must be normalized (ie: each country has one row)
    country must be the first column
    countries must be spelled in entirety with standard convention
        ie: United States, not USA/US/America (casing insensitive)
    "region" must be the second column
        - can be continent or any broader generalization of country
    subsequent columns are the attributes
    all other attributes must be numerical
    file must contain at least five data points

For extensibility purposes, potential ideas include data plugins for another API, txt format, 
or un-normalized csv files (ie: each country has five different rows). 

### Visualization Plugins

Currently we have implemented three visualization plugins. The group only needs two, but we were 
unsure whether the bar and pie chart were too similar in implementation (only one method was different).
The first two visualizations uses a piece of numerical data and displays it across regions 
(ie: comparing GDP between Oceania, Polynesia, etc).

The last visualization is a bubble map that also compares a single numerical field, but across countries 
throughout the world. NOTE: the bubbles not appearing for the API/Bubble Map combo is NOT a bug- 
the first five entries of the API are countries not supported by plotly.js. The implementation here
is a bit more difficult because the country names must be converted to its ISO3 code first. The size
of the bubbles are determined based on the proportion of the value compared to the sum of all the values.

The visualizations all use plotly.JS, which constructs the charts based on a javascript string that is
passed through. More information can be found here: https://plotly.com/javascript/.

The site also shows how to generate the specific JS string per visualization type. That being said, 
the most important method to change in the visualization plugins is the getExtraJS method that creates
the JS string. However, depending on how the data should be read into the visualization, custom data
cleaning methods may be necessary as well- and is not provided in the interface, since they are 
plugin-specific.

The best approach to create new visualization plugins is to pick a chart from the link provided and
perform the respective data cleaning and JS string needed. A possible choice here is to implement
the scatterplot- but two numerical fields will be needed instead than one.

### User Interface Implementation
The App class contains methods for loading all the data and visualization plugins, so there are no
modifications needed in the App, ScreenDisplay, or optionsScreen.hbs files. However, after
implementing the interfaces, the file path must be added in the META-INF.services folder.