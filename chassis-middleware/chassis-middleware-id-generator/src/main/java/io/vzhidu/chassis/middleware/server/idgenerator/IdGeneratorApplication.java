package io.vzhidu.chassis.middleware.server.idgenerator;

import io.vzhidu.chassis.common.scapp.AbstractChassisSpringBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application Entry of Identifier service
 *
 * @author Zhiqiang Du Created at 2020/3/18
 */
@SpringBootApplication
public class IdGeneratorApplication extends AbstractChassisSpringBootApplication {

    /**
     * Main method
     *
     * @param args application arguments
     */
    public static void main(String[] args) {
        new IdGeneratorApplication().run(args);
    }
}
