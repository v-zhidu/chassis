package io.vzhidu.chassis.common.scof;

import io.vzhidu.chassis.common.scapp.AbstractChassisSpringBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 测试Spring Boot应用
 *
 * @author Zhiqiang Du created at 2021/5/12
 */
@EnableFeignClients(defaultConfiguration = FeignClientCustomizeConfiguration.class)
@SpringBootApplication
public class SpringFeignClientTestApplication extends AbstractChassisSpringBootApplication {

    public static void main(String[] args) {
        new SpringFeignClientTestApplication().run(args);
    }
}
