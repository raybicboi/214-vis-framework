package edu.cs.visframework.dao;

import edu.cs.visframework.pojo.Value;

import java.util.List;

/**
 * NLP logic actually translate each Value into a float
 * indicating the sentimental rate, and adding the float into
 * the field of Value.
 *
 * We could further expand the logic with different tasks / libraries
 * which is the main reason we put this into an interface.
 *
 * @author Shicheng Huang
 */
public interface NlpLogic {
    List<Value> getAnaylzedData(List<Value> input);
}
