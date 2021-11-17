#Domain:
The domain of framework is numerical data that has to do with countries. We will be using https://ourworldindataorg.docs.apiary.io/#reference as an API to interact with our world in data’s database. We have also considered Country API by API Ninjas as an alternate source. These APIs contain data on countries that can be cross-compared. They use common metrics, such as GDP, employment, emissions, life expectancy, and more. We believe this format of information is quite common and is easily extensibile to new data plugins and APIs that contain UN data. Additionally, our plugins will support local CSV files extracted from https://data.un.org/Default.aspx .

Typically, these types of data sets involve quantitative features, such as life expectancy and categorical data, such as countries within a continent (obviously). The framework will involve translating data into visualizations that can display comparisons between two features- one quantitative and one categorical. We will also provide the ability to focus on one country over a specified time period.

There is a lot of room for reuse, since it is quite common to display data that compares counts of a field based on a category. The visualization plugins should also be extensibile because other visualization types, such as bar charts and pie charts, are also common ways to display the comparison of counts by category. The framework should be able to support datasets in both csv and json (through API) formats. Two different data plugins will be implemented to do this- one that is specific to the API, and another that parses a csv converted dataset from a different source.

#Generality vs. Specificity:
The framework is unique in that it displays information about multiple kinds of data: categorical and numerical. This is very generalizable. Our data opportunities are expansive, but still within the scope of country-related data. For example, data could be the current population/GDP/crime rate of each country, or could be any of those statistics for a given country over a time period.

The visualizations of choice will tentatively be a bubble graph and a heat map- as it is a unique type of graph while still being relevant to the data plugins used. Of course, a bar graph will always work for these types of datasets, but it is very basic and may be too broad to provide any value. In any case, the bar chart can always be an additional plugin for other groups to implement for milestone C.

For the bubble chart, the quantitative data can be compared among data points based on the size of the bubble (optionally including time as an axis) while the qualitative data can be seen through the colors of the bubbles. A heat map can show densities of quantitative data overlaid onto a world map.

#Project Structure:
The project will be split into three components:
The data plugins- which parses different dataset types into a generic format. The data plugin will also use APIs and third party libraries to extract relevant data.
The visualization plugins- which presents the generic form data into a visualization
The framework- which serves as a pipeline that transitions the raw dataset through the data plugins and out into the visualization plugins. The framework should also be able to handle some portions of the data cleaning that is outside the scope of what the generic data plugins should be responsible for.

The project will be structured with a framework package within the java source file. Within the framework package, there will be a visualization package and a data package, where the respective plugin interfaces/classes will be stored. Within the framework package, the framework class that contains the main method will be stored. Concurrent with java source file will be a resources folder that contains the metadata folder with all of the plugins being used. That folder will also contain the handlebars gui file, if we decide to use that for the front-end.

We will be using two maps to store the information about our data. The first map will be of the form key |-> value where keys are objects and values are the numerical data associated with this. The second map is of the form key |-> value where the values are categories and the values are sets of objects. For example, for categories being continents, objects being countries, and numerical data being GDP, the second map would map continents to the countries in the continent.
#Plugin Interfaces:
##Data Plugin
###Data Setup/Endpoint
* onRegister(Framework framework) - optional set up when the plugin is registered
* onEnd() - optional work to be done by the plugin at the end of its life cycle

###Data Extraction
* importData(File file, String fileType) - uses a library (ie: openCSV) to import
* getOptions(ArrayList<String> options) - retrieves the fields of the imported data based on the user input
* getCountry(ArrayList<String> countries) - retrieve the rows of data that applies to the countries based on the user input
* extractData(String dataType) - prepares the numerical data based on user preferences for data cleaning
* extractData(String dataType, String startYear, String endYear) - prepares the numerical data within a specified time period
* extractCountryData(String dataType, String country, String startYear, String endYear) - prepares the numerical data within a specified time period
* extractCategoricalData() - prepares the categorical data for the framework. Will return a map from continents to sets of countries.
* getFields() : List<String> - gets the relevant data fields from the user

###Data Modification
* getCountries() - retrieves the list of countries
* assignContinent() - adds a field that assigns the respective continent for each row of data

##Visualization Plugin
* filterByCat(String field, String value) - filters the visualization based on specific value per category (ie: only show values for countries in North America)
* setDataType(String dataType) - displays information about in the current format for the given numerical data
* filterByValue(String field, ArrayList<int> range) - filters the visualization based on a range of values for a specific field (ie: only show countries with an infant mortality rate between 0-5 percent)
* undoFilters() - undoes the filters and display all information originally designed for the data display
* chooseData(String dataType) - displays the relevant data based on the numerical data type chosen
* chooseDataInRange(String dataType, String startYear, String endYear)
* getValues() - returns a map from countries to the current numerical data in use

