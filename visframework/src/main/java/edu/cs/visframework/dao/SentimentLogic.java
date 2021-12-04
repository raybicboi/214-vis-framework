package edu.cs.visframework.dao;

import edu.cs.visframework.pojo.Value;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SentimentLogic implements NlpLogic{
    private static final StanfordCoreNLP pipeline;

    static {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);
    }

    public SentimentLogic(){
    }

    /**
     * Get sentiment data processed by nlp
     * must run export MAVEN_OPTS="-Xmx14000m" and mvn compile before run
     * @param input list of Value
     * @return list of Value
     */
    @Override
    public List<Value> getAnaylzedData(List<Value> input) {
        Annotation annotation;
        List<Value> output = new ArrayList<>();

        for (Value v:input){
            String text = v.getText();
            annotation = pipeline.process(text);

            List<Double> sentiments = new ArrayList<>();

            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {

                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);

                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                sentiments.add((double) sentiment);
            }

            Value outputEntry = new Value(v.getTime(), v.getText(), 2);
            double meanSentiment = sentiments.stream().mapToDouble(Double::doubleValue).summaryStatistics().getAverage();
            outputEntry.setScore(meanSentiment);
            output.add(outputEntry);
        }

        return output;
    }
}
