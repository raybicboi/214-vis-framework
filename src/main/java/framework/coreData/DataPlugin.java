package framework.coreData;

import java.util.Date;

public interface DataPlugin {

    public void importData(String url);

    public void getOptions(int[] options);

    public void extractData();

    public void extractData(Date start, Date end); // convert time to Date type

}
