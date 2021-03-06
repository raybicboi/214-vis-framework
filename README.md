## Additional Readme from team-dk

# Added Data Plugins

The two data plugins added is the InstagramDataPlugin and the CSVDataPlugin. Both files are in the 
src/edu/cs/visframework/dao directory, same as the other plugins left from the original group.

When launching the application, for "Data source name", you can use the existing plugins, along with 'instagram'
and 'csv'. The instructions for launch has not changed from the original implementation.

# Instagram Plugin

To use the instagram plugin, the data source url can be one of three formats: #hashtag, hashtag, or @username. 
For hashtag queries, captions of recent posts under that hashtag are analyzed. For username queries, the comments 
under that user’s recent posts are analyzed. Will not work with private accounts. To see data across more of a 
time range, try less-common hashtags or public accounts that don’t post often.

# CSV Plugin

The data source url is just the name of the csv file in the directory (including '.csv'). It must be within the visframework directory
to work. I provided an example test.csv in that directory that contains a little over ten data points from the 
dogs subreddit for demonstration purposes. You can also place your csv file in a deeper directory, but the data
source url needs to be the correct path from visframework folder as the source. If you type in a path that does
not exist in the directory, the app will not crash- but you will get an empty graph.

Mac users can try going into the CSVDataPlugin class and switching out lines 44 and 45 if there are pathing issues
since the directory slashes are different.

The format of the CSV is as follows:
- Text        | Timestamp
- sampleText1 | sampleTimeStamp1
- sampleText2 | sampleTimeStamp2
- ...

Where there must be a header row, and each row only contains two elements. The text can have quotes or no quotes,
and can also contain commas (even though we are reading csv). 

The timestamp must be formatted as follows: xxxx-xx-xx@ (year-month-date), where x are digits, delimited by a 
dash("-"). The "@" can be any character or multiple characters. It is needed just because the original implementation
requires the length of the timestamp to be at least 11 (which may or may not be intentional).

# Added Visualization Plugin

The visualiation plugin used in the time series plugin, which is in the same 'dao' folder what contains all the
other plugins. For the chart name, just write 'timeseries'.

The graph is a line graph of five lines (for five degree of sentiments) that compares the relative frequency of 
each the five levels across months.

If the instagram chart is empty, it's most likely a limitation in the API (ie: does not register that user). If
the csv chart is empty, it's most likely due to a formatting issue of the data or the file doesn't exist/in 
the wrong directory.

# Original Readme from group teamhhs
# github link

https://github.com/CMU-17-214/hw6-analytics-framework-teamhhs/commit/ab6c209cf78b0347aaafdcae21e72badee941f04

# Bonus

We would like to apply for the following bonus points

- 20: ML library data processing 
- 5: git action 
- 5: git pull request

# Usage

- pre-req
  - make sure you have node.js and npm installed in your local env.
  - 
  - make sure you have IDE with mvn support
  - make sure localhost port 3000 & 4000 are free to use

## Download 

- `git clone <our_repo>`

## Front-end

- `cd <repo_root>`
- `cd front_end`
- `npm install`
- `npm start `

### Back-end

- load project in IDE(IDEA recommened)
- run the project
- NLP library is extremely slow, expecting 10-30s for a single graph with 20 entries to process





# Idea

## Framework

- The idea of the project is to implement a sentimental analysis framework. 
  - The idea is to perform sentiment analysis (how positive the tone of some text is) on different texts. The framework performs the sentiment analysis on text from different sources (provided by data plugins) and shows results in different ways (using visualization plugins). The framework performs the sentiment analysis itself, hence provides benefits for reuse. Further reuse benefits come from being able to reuse existing visualizations when just providing a new data plugin or vice versa.

- The framework is extensible in terms of 
  - Data plugins: Add more data sources to anaylsis.
  - Visualization plugins:  Add more graphs.

- The framework consists of two parts: 

  - Front-end: We use `react` for front-end design, that takes in the user requirements and send a JSON request to the back-end. When Back-end returns the data, we parse that data and use `plotly` to create graphs.

  - Back-end: We use `SpringBoot` for back-end design, take takes in JSON input and 

    0) parse input json object
    1) allocate data plugins
    2) download data using data plugins
    3) allocate process plugins
    4) process data using process plugins (filter)
    5) allocate NLP plugins
    6) process data using ML algorithms
    7) allocate visualization plugins
    8) load data to JSON object with the correct format for plotting.

- **What do you need to do to add plugins ?** 

  - Data plugin: 

    - Implement `dataPlugin` interface (see code documentation for more details)

    - add your implementation into `beanFactory.xml`

      ```xml
      <!-- example adding data plugins -->
      <bean name="<your_datasource_name>DataPlugin"
            class="edu.cs.visframework.dao.<your_implementation>">
      </bean>
      ```

      

  - Visualization plugin:

    - Implement `visualizationPlugin` interface (see code documentation for more details)

    - add your implementation into `beanFactory.xml`

      ```xml
      <!-- example adding data plugins -->
      <bean name="<your_graph_name>VisPlugin"
            class="edu.cs.visframework.dao.<your_implementation>">
      </bean>
      ```

  - NOTE: 

    - <your_datasource_name> or <your_graph_name> is coupled to the front-end input (user needs to input the exact name of your choice in order to use your plugin) 

      For example: 

      if you want user to type`youtube` as data source, you need to set <your_datasource_name> as `youtube`

      if you want user to type`new_graph` as graph name, you need to set <your_graph_name> as `new_graph`

      

  

- Design patterns:

  - We use MVC pattern for back-end design that separates controller and the actual service implementation.

  - We use factory pattern to allocate plugins dynamically.

  - We use strategy pattern for data/graph plugins for extensibility and flexibility.

    

## Plugin implementations

### Data plugins

#### YouTube Plugin
Take video id as dataSourceUrl, i.e. for a video whose url is https://www.youtube.com/watch?v=Ku2zZ5Vfpzo, the input should be Ku2zZ5Vfpzo, return 20 comment entries by default.

#### Twitter Plugin
Twitter plugin takes in a username as the data source and searches the latest 100 posts from the user. For example, 
To search posts from Elon Musk (Twitter username : @elonmusk), we should enter "elonmusk" as a string.  

#### News Plugin
News plugin takes in a single word as the search key word. The plugin will then return descriptions of the first 20 news 
related to the given keyword. For example, to search news related to CMU, we should enter "cmu" as a string. 

### Visualization plugins

#### Pie Plugin
Visualize the portions of all 5 sentiments.   
![20211122224317](design/20211122224317.png)

#### Heatmap Plugin
Visualize the occurrence time of each sentiment state in each month
![20211122224713](design/20211122224713.png)

#### Bar chart Plugin
Visualize the sentiment rates for selected data in ordered dates.
![bar_graph_example](design/bar_graph_example.PNG)