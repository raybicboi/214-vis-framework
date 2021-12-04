package edu.cs.visframework.pojo;

/**
 * Value for our intermediate processed data.
 *
 * @author Shicheng Huang, Yuhao Hu
 */
public class Value implements Comparable<Value>{
    /**
     * Date for data source.
     */
    private final String time;

    /**
     * Data source content.
     */
    private final String text;

    /**
     * Sentimental analysis result for content.
     */
    private double score;


    /**
     * Public constructor
     * @param time time
     * @param text text
     * @param score score
     */
    public Value(String time, String text, double score) {
        this.time = time.substring(0, 10).replace("-","/");
        this.text = text;
        this.score = score;
    }

    /**
     * Setters & Getters
     */
    public void setScore(double score) {
        this.score = score;
    }
    public double getScore() { return this.score;}
    public String getTime(){ return this.time;}
    public String getText() { return this.text;}

    /**
     * We would like to sort by time, so implemented compareTo.
     * @param o compared value.
     * @return compared
     */
    @Override
    public int compareTo(Value o) {
        return this.time.compareTo(o.getTime());
    }

    /**
     * Print to String.
     * @return String.
     */
    @Override
    public String toString() {
        return String.format("time: "+ time + ", score: " + score);
    }
}
