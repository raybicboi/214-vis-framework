package edu.cs.visframework.config;

import edu.cs.visframework.controller.ParamController;
import edu.cs.visframework.dao.BarPlugin;
import edu.cs.visframework.dao.DataPlugin;
import edu.cs.visframework.dao.FilterLogic;
import edu.cs.visframework.dao.HeatMapPlugin;
import edu.cs.visframework.dao.NewsDataPlugin;
import edu.cs.visframework.dao.NlpLogic;
//import edu.cs.visframework.dao.PlotPlugin;
import edu.cs.visframework.dao.PiePlugin;
import edu.cs.visframework.dao.ProcessLogic;
import edu.cs.visframework.dao.SentimentLogic;
import edu.cs.visframework.dao.TwitterDataPlugin;
import edu.cs.visframework.dao.YtDataPlugin;
import edu.cs.visframework.service.DataService;
import edu.cs.visframework.service.ParserService;
import edu.stanford.nlp.ling.TaggedWord;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


/**
 * Spring config class with global IOC config.
 * This is the class where we put Plugins / Logics into global context.
 *
 * @author Shicheng Huang
 */
@Configuration
public class SpringConfig {
    @Bean
    public FilterLogic filterLogic() {
        return new FilterLogic();
    }

    @Bean
    public SentimentLogic sentimentLogic() {
        return new SentimentLogic();
    }

    @Bean
    public BeanFactory xmlBeanFactory() {
        Resource res = new ClassPathResource("beanFactory.xml");
        BeanFactory factory = new XmlBeanFactory(res);
        return factory;
    }
}
