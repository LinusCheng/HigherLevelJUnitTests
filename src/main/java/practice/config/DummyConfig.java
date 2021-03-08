package practice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DummyConfig {

    @Value("${moneyOrder.branch}")
    private String branch;

    @Bean
    public void checkPropertyIsLoaded(){
        System.out.println("==== check branch -> " + branch);
    }

}
