package io.vzhidu.chassis.common.scof;

import feign.Logger;
import feign.codec.ErrorDecoder;
import io.vzhidu.chassis.common.scof.feign.InfoFeignLoggerFactory;
import io.vzhidu.chassis.common.scof.feign.ScappErrorDecoder;
import org.springframework.cloud.openfeign.DefaultFeignLoggerFactory;
import org.springframework.cloud.openfeign.FeignLoggerFactory;
import org.springframework.context.annotation.Bean;

/**
 * Feign Client 配置
 *
 * @author Zhiqiang Du
 */
public class FeignClientCustomizeConfiguration {

    /**
     * 替换默认的{@link DefaultFeignLoggerFactory}
     */
    @Bean
    public FeignLoggerFactory feignLoggerFactory() {
        return new InfoFeignLoggerFactory();
    }

    /**
     * 默认的feign日志级别, 需要开启日志级别为DEBUG
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    /**
     * 配置通用的feign处理方法
     */
    @Bean
    public ErrorDecoder scappErrorDecoder() {
        return new ScappErrorDecoder();
    }
}
