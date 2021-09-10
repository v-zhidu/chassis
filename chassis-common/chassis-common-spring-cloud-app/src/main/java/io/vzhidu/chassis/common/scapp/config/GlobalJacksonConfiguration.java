package io.vzhidu.chassis.common.scapp.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Class {@link GlobalJacksonConfiguration} holds all configurations that used by all the system services.
 *
 * @author Zhiqiang Du Created at 2020/6/29
 */
@Configuration
public class GlobalJacksonConfiguration {

    /**
     * 配置全局的{@link ObjectMapper}
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule())
                .setDateFormat(new StdDateFormat())
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }
}
