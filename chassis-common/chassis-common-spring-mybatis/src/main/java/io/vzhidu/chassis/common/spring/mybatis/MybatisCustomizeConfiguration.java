package io.vzhidu.chassis.common.spring.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 通用mybatis配置
 *
 * @author Zhiqiang Du created at 2021/10/1
 */
@Configuration(proxyBeanMethods = false)
@MapperScan("io.vzhidu.chassis.**.mapper")
@ConditionalOnProperty(prefix = "mybatis", name = "config-location")
public class MybatisCustomizeConfiguration {

    private final ApplicationContext context;

    private final DataSource dataSource;

    /**
     * 通用mybatis配置
     */
    public MybatisCustomizeConfiguration(ApplicationContext context, DataSource dataSource) {
        this.context = context;
        this.dataSource = dataSource;
    }

    /**
     * Return an instance of {@link SqlSessionFactory}
     * <p>
     * 针对mybatis的全局配置
     */
    @Bean
    SqlSessionFactory sqlSessionFactory(@Value("${mybatis.config-location}") String configLocation,
                                        @Value("${mybatis.mapper-locations}") String mapperLocations) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setConfigLocation(context.getResource(configLocation));
        bean.setMapperLocations(context.getResources(mapperLocations));

        return bean.getObject();
    }

    /**
     * Mybatis Java Config, 不可变的配置可以放到这里
     */
    @Bean
    ConfigurationCustomizer mybatisConfigurationCustomize() {
        return configuration -> {
        };
    }
}
