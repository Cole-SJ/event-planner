package edu.gsu.eventplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class EventPlannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventPlannerApplication.class, args);
    }

}
