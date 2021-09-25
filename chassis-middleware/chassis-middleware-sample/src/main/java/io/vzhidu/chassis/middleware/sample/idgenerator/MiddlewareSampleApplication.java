package io.vzhidu.chassis.middleware.sample.idgenerator;

import io.vzhidu.chassis.common.scapp.AbstractChassisSpringBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * ID 生成服务示例程序
 *
 * @author Zhiqiang Du Created at 2020/9/1
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "io.vzhidu.chassis")
public class MiddlewareSampleApplication extends AbstractChassisSpringBootApplication {

    public static void main(String[] args) {
        new MiddlewareSampleApplication().run(args);
    }
}
