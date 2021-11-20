package io.vzhidu.chassis.middleware;

import io.vzhidu.chassis.common.spring.web.AbstractSpringBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ID 生成服务示例程序
 *
 * @author Zhiqiang Du Created at 2020/9/1
 */
@SpringBootApplication
public class MiddlewareSampleApplication extends AbstractSpringBootApplication {

    public static void main(String[] args) {
        new MiddlewareSampleApplication().run(args);
    }
}

