package com.enterprise.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String... args) {
        final ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        LOGGER.info("Beans loaded {}", context.getBeanDefinitionCount());
        LOGGER.info("Application started");
    }
}
