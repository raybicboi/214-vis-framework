#Domain:
Our framework will involve translating data into a bubble chart that can visualize the comparison between two features- one quantitative and one categorical. The quantitative data can be compared among data points based on the size of the bubble, while the qualitative data can be seen through the colors of the bubbles. The ideal use of the framework is with data that contains quantitative data across states or countries, or any other dataset with obvious categorical groups. Examples of visualizations that are compatible with the bubble chart include comparing the population of each country, or showing crime rate in each state.

There is a lot of room for reuse, since it is quite common to display data that compares counts of a field based on a category. The visualization plugins should also be extensibile because other visualization types, such as bar charts and pie charts, are also common ways to display the comparison of counts by category. The framework should be able to support datasets in both csv and json formats. Two different data plugins will be implemented to do this.

#Generality vs. Specificity:
The framework is unique in that it displays information about multiple kinds of data: categorical and numerical. This is very generalizable. For example, data can be (country, *some property, e.g. GDP*) and the categories are continents, or the data can be (NBA teams, average wins for the past decade) and the categories could be divisions or conferences.

#Project Structure:
The project will be split into three components:
The data plugins- which parses different dataset types into a generic format. The data plugin will also use APIs and third party libraries to extract relevant data.
The visualization plugins- which presents the generic form data into a visualization
The framework- which serves as a pipeline that transitions the raw dataset through the data plugins and out into the visualization plugins. The framework should also be able to handle some portions of the data cleaning that is outside the scope of what the generic data plugins should be responsible for.

The project will be structured with a framework package within the java source file. Within the framework package, there will be a visualization package and a data package, where the respective plugin interfaces/classes will be stored. Within the framework package, the framework class that contains the main method will be stored. Concurrent with java source file will be a resources folder that contains the metadata folder with all of the plugins being used. That folder will also contain the handlebars gui file, if we decide to use that for the front-end.

We will be using two maps to store the information about our data. The first map will be of the form key |-> value where keys are objects and values are the numerical data associated with this. The second map is of the form key |-> value where the values are categories and the values are sets of objects. For example, for categories being continents, objects being countries, and numerical data being GDP, the second map would map continents to the countries in the continent.

#Plugin Interfaces:

###Data Setup/Endpoint
onRegister(Framework framework) - optional set up when the plugin is registered
onEnd() -

###Data Extraction
* importData(File file, String fileType) - uses a library (ie: openCSV) to import
* getOptions(ArrayList<String> options) - retrieves the fields of the imported data based on the user input
* getCountry(ArrayList<String> countries) - retrieve the rows of data that applies to the countries based on the user input
* extractData() - prepares the numerical data based on user preferences for data cleaning
* extractCategoricalData() - prepares the categorical data for the framework. Will return a map from continents to sets of countries.
* getFields() - gets the relevant data fields from the user

###Data Modification
* getCountries() - retrieves the list of countries
* getFields() - retrieves the list of selected fields
* assignContinent() - adds a field that assigns the respective continent for each row of data
* assignNull(String field, Object default) - replaces null values for each datapoint of a select field with a default value (ie: replace null populations with 0)
* removeNull(ArrayList<String> fields) - remove data points based on whether the chosen fields are null

###Data Display
* filterByCat(String field, String value) - filters the visualization based on specific value per category (ie: only show values for countries in North America)
* filterByValue(String field, ArrayList<int> range) - filters the visualization based on a range of values for a specific field (ie: only show countries with an infant mortality rate between 0-5 percent)
* undoFilters() - undoes the filters and display all information originally designed for the data display
#