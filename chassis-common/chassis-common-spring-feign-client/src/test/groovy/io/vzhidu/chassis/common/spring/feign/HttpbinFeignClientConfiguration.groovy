package io.vzhidu.chassis.common.spring.feign

import feign.Client
import feign.Logger
import okhttp3.OkHttpClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryFactory
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient
import org.springframework.cloud.openfeign.loadbalancer.OnRetryNotEnabledCondition
import org.springframework.cloud.openfeign.loadbalancer.RetryableFeignBlockingLoadBalancerClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Conditional

class HttpbinFeignClientConfiguration {

    /**
     * 默认的日志级别为FULL
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL
    }

    @Bean
    @Conditional(OnRetryNotEnabledCondition.class)
    Client feignClient(OkHttpClient okHttpClient,
                       LoadBalancerClient loadBalancerClient,
                       LoadBalancerClientFactory loadBalancerClientFactory) {
        OkHttpClient newClient = okHttpClient.newBuilder().addInterceptor(new HttpbinResponseInterceptor()).build()
        feign.okhttp.OkHttpClient delegate = new feign.okhttp.OkHttpClient(newClient)
        return new FeignBlockingLoadBalancerClient(delegate, loadBalancerClient, loadBalancerClientFactory)
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.retry.support.RetryTemplate")
    @ConditionalOnBean(LoadBalancedRetryFactory.class)
    @ConditionalOnProperty(value = "spring.cloud.loadbalancer.retry.enabled", havingValue = "true",
            matchIfMissing = true)
    Client feignRetryClient(LoadBalancerClient loadBalancerClient,
                            OkHttpClient okHttpClient,
                            LoadBalancedRetryFactory loadBalancedRetryFactory,
                            LoadBalancerClientFactory loadBalancerClientFactory) {
        OkHttpClient newClient = okHttpClient.newBuilder().addInterceptor(new HttpbinResponseInterceptor()).build()
        feign.okhttp.OkHttpClient delegate = new feign.okhttp.OkHttpClient(newClient)
        return new RetryableFeignBlockingLoadBalancerClient(delegate, loadBalancerClient,
                loadBalancedRetryFactory, loadBalancerClientFactory)
    }
}
