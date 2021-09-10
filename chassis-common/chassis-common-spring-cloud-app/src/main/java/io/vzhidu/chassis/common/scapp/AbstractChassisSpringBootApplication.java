package io.vzhidu.chassis.common.scapp;

import io.vzhidu.chassis.common.scapp.config.ChassisApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;

/**
 * Abstract Spring Boot Application, intend to handler errors globally.
 *
 * @author Zhiqiang Du Created at 2019/9/27
 */
@Slf4j
@SpringBootApplication(scanBasePackages = {"io.vzhidu.chassis"})
@EnableConfigurationProperties({ChassisApplicationProperties.class})
public abstract class AbstractChassisSpringBootApplication {

    @Autowired
    private ChassisApplicationProperties properties;

    /**
     * Check properties after startup
     */
    @PostConstruct
    public void init() {
        log.info("Starting application with properties: {}", properties.toString());
    }

    private final SpringApplication app;

    /**
     * Default Constructor
     */
    protected AbstractChassisSpringBootApplication() {
        this.app = new SpringApplicationBuilder(getClass()).build();
    }

    /**
     * Global application entry
     *
     * @param args application arguments
     */
    public void run(String[] args) {
        app.run(args);
    }
}
