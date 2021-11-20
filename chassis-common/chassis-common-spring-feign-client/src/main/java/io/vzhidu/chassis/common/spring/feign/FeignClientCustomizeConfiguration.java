package io.vzhidu.chassis.common.spring.feign;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * 扫描全局的@FeignClient
 *
 * @author Zhiqiang Du
 */
@Configuration(proxyBeanMethods = false)
@EnableFeignClients(basePackages = "io.vzhidu.chassis", defaultConfiguration = DefaultFeignClientConfiguration.class)
class FeignClientCustomizeConfiguration {

}
