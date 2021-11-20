package io.vzhidu.chassis.common.spring.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient;
import org.springframework.cloud.openfeign.loadbalancer.OnRetryNotEnabledCondition;
import org.springframework.cloud.openfeign.loadbalancer.RetryableFeignBlockingLoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

import javax.validation.Validation;

/**
 * Feign Client 全局默认配置
 * <p>
 * 注意：
 * 1. 优先级要低于application-feign.yml中的配置, 区别在于代码方式对全局feign client生效
 * 2. 不可变的配置参考{@link FeignClientCustomizeConfiguration}, 覆盖需要定制的bean即可，不影响其他bean生效,
 * 3. @FeignClient需指定具体的configuration，@EnableFeignClients#defaultConfiguration会导致其他的配置类失效
 * 4. 根据不同环境可变的配置，通过覆盖application-feign.yml中的配置实现
 * 4. Spring Cloud OpenFeign暂不支持异步请求
 * 5. 禁止覆盖{@link Client}，否则会导致自动配置的load balancer和circuit breaker失效
 *
 * <a href="https://github.com/spring-cloud/spring-cloud-openfeign/blob/v2.2.3.RELEASE/docs/src/main/asciidoc<br>
 * /spring-cloud-openfeign.adoc#reactive-support></a>
 *
 * @author Zhiqiang Du created at 2021/10/11
 */
public class DefaultFeignClientConfiguration {

    /**
     * 默认的feign日志级别
     */
    @Bean
    @ConditionalOnMissingBean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    /**
     * 默认的请求编码器, Json序列化器可通过覆盖该方法修改
     */
    @Bean
    @ConditionalOnMissingBean
    public Encoder encoder(ObjectMapper objectMapper) {
        return new JacksonEncoder(objectMapper);
    }

    /**
     * 默认的响应解码器, Json序列化器可通过覆盖该方法修改
     */
    @Bean
    @ConditionalOnMissingBean
    public Decoder decoder(ObjectMapper objectMapper) {
        return new JacksonDecoder(objectMapper);
    }

    /**
     * 默认的的feign错误处理方法
     */
    @Bean
    @ConditionalOnMissingBean
    public ErrorDecoder errorDecoder(ObjectMapper objectMapper) {
        return new DefaultErrorDecoder(objectMapper);
    }

    /**
     * 默认的重试配置, 拦截{@link RetryableException}并重试
     */
    @Bean
    @ConditionalOnMissingBean
    public Retryer retryer() {
        return new DefaultRetryer(DEFAULT_RETRY_PERIOD_IN_MILLS,
                DEFAULT_MAX_RETRY_PERIOD_IN_MILLS, DEFAULT_MAX_RETRY_ATTEMPTS);
    }

    /**
     * 默认的请求拦截器
     */
    @Bean
    public RequestInterceptor traceRequestInterceptor() {
        return new HeadersRequestInterceptor();
    }

    /**
     * 默认的{@link Feign.Builder}
     */
    @Bean
    public Feign.Builder builder() {
        return ValidationFeign.builder(Validation.buildDefaultValidatorFactory().getValidator())
                .invocationHandlerFactory(new InvocationHandlerFactory.Default());
    }

    /**
     * 默认的OkHttpClient配置
     * <p>
     * 注意：使用@FeignClient#configuration配置OkHttpClient不生效
     * 原因在于新的OkHttpClient Bean并不会影响已经生成的{@link Client}
     * If Spring Cloud LoadBalancer is on the classpath,
     * FeignBlockingLoadBalancerClient is used. If none of them is on the classpath, the default feign client is used.
     */
    @Bean
    @ConditionalOnMissingBean
    @Conditional(OnRetryNotEnabledCondition.class)
    public Client feignClient(okhttp3.OkHttpClient okHttpClient, LoadBalancerClient loadBalancerClient,
                              LoadBalancerProperties properties, LoadBalancerClientFactory loadBalancerClientFactory) {
        return new FeignBlockingLoadBalancerClient(buildOkHttpClient(okHttpClient),
                loadBalancerClient, properties, loadBalancerClientFactory);
    }

    /**
     * 客服端可重试实现
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "org.springframework.retry.support.RetryTemplate")
    @ConditionalOnBean(LoadBalancedRetryFactory.class)
    @ConditionalOnProperty(value = "spring.cloud.loadbalancer.retry.enabled", havingValue = "true",
            matchIfMissing = true)
    public Client feignRetryClient(LoadBalancerClient loadBalancerClient, okhttp3.OkHttpClient okHttpClient,
                                   LoadBalancedRetryFactory loadBalancedRetryFactory, LoadBalancerProperties properties,
                                   LoadBalancerClientFactory loadBalancerClientFactory) {
        return new RetryableFeignBlockingLoadBalancerClient(buildOkHttpClient(okHttpClient),
                loadBalancerClient, loadBalancedRetryFactory, properties, loadBalancerClientFactory);
    }

    private static feign.okhttp.OkHttpClient buildOkHttpClient(okhttp3.OkHttpClient okHttpClient) {
        OkHttpClient newClient = okHttpClient.newBuilder()
                .build();
        return new feign.okhttp.OkHttpClient(newClient);
    }

    /**
     * 默认的重试间隔，会按照1.5倍执行退避重试
     */
    protected static final long DEFAULT_RETRY_PERIOD_IN_MILLS = 100L;

    /**
     * 默认的重试最大间隔，退避大于1.5倍后，会按照最大重试间隔重试
     */
    protected static final long DEFAULT_MAX_RETRY_PERIOD_IN_MILLS = 1000L;

    /**
     * 默认的最大重试次数
     */
    protected static final int DEFAULT_MAX_RETRY_ATTEMPTS = 5;
}
