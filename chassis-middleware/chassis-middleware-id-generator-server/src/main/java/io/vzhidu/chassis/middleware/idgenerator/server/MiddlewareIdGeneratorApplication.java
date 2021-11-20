package io.vzhidu.chassis.middleware.idgenerator.server;

import io.vzhidu.chassis.common.spring.web.AbstractSpringBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application Entry of Identifier service
 *
 * @author Zhiqiang Du Created at 2020/3/18
 */
@SpringBootApplication
public class MiddlewareIdGeneratorApplication extends AbstractSpringBootApplication {

    /**
     * Main method
     *
     * @param args application arguments
     */
    public static void main(String[] args) {
        new MiddlewareIdGeneratorApplication().run(args);
    }
}
