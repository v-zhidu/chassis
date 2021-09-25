package io.vzhidu.chassis.common.scof.feign;

import feign.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignLoggerFactory;

/**
 * 工厂方法，创建{@link InfoFeignLoggerFactory}
 *
 * @author Zhiqiang Du created at 2021/5/13
 */
public class InfoFeignLoggerFactory implements FeignLoggerFactory {

    /**
     * 创建{@link Logger}实例
     */
    @Override
    public Logger create(Class<?> type) {
        return new InfoSlf4jLogger(LoggerFactory.getLogger(type));
    }
}
