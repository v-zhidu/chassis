package io.vzhidu.chassis.common.spring.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * {Description}
 *
 * @author Zhiqiang Du created at 2021/10/12
 */
@SpringBootApplication
public class TestApplication extends AbstractSpringBootApplication {

    public static void main(String[] args) {
        new TestApplication().run(args);
    }
}
