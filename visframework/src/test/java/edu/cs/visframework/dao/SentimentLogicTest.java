package edu.cs.visframework.dao;

import edu.cs.visframework.pojo.Value;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SentimentLogicTest {
    @Test
    public void testSentimentProcess(){
        List<Value> input = new ArrayList<>();
        input.add(new Value("0000000000", "happy", 2));
        input.add(new Value("0000000000", "sad", 2));

        SentimentLogic nlp = new SentimentLogic();
        List<Value> output = nlp.getAnaylzedData(input);

        assertTrue(output.get(0).getScore() > 2);
        assertTrue(output.get(1).getScore() < 2);
    }
}