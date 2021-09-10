package io.vzhidu.chassis.common.scapp;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Mock spring boot application extend with {@link AbstractChassisSpringBootApplication}
 *
 * @author Zhiqiang Du Created at 2020/3/18
 */
@SpringBootApplication
public class MockSpringBootApplication extends AbstractChassisSpringBootApplication {

    public static void main(String[] args) {
        new MockSpringBootApplication().run(args);
    }
}
