package io.vzhidu.chassis.common.spring.stream;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 全局Spring-Cloud-Stream配置
 *
 * @author Zhiqiang Du created at 2021/10/19
 */
@Configuration
public class StreamCustomizeConfiguration {

    /**
     * 全局默认的StreamBridge
     *
     * @param streamBridge {@link StreamBridge}
     * @return {@link MessageStreamBridge}
     */
    @Bean
    @ConditionalOnMissingBean
    MessageStreamBridge messageStreamBridge(StreamBridge streamBridge) {
        return new GenericMessageStreamBridge(streamBridge);
    }
}
