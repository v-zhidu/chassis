package io.vzhidu.chassis.middleware.server.idgenerator.config;

import com.sankuai.inf.leaf.IDGen;
import com.sankuai.inf.leaf.common.ZeroIDGen;
import com.sankuai.inf.leaf.segment.SegmentIDGenImpl;
import com.sankuai.inf.leaf.segment.dao.IDAllocDao;
import com.sankuai.inf.leaf.segment.dao.impl.IDAllocDaoImpl;
import com.sankuai.inf.leaf.snowflake.SnowflakeIDGenImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Identifier Generator 配置数据源，美团IdGen
 *
 * @author Zhiqiang Du Created at 2020/8/21
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(IdGeneratorConfigurationProperties.class)
public class IdGeneratorConfiguration {

    private final IdGeneratorConfigurationProperties properties;

    public IdGeneratorConfiguration(IdGeneratorConfigurationProperties properties) {
        this.properties = properties;
    }

    /**
     * 号段id服务配置
     *
     * @param dataSource {@link DataSource}
     * @return {@link IDGen} generator with qualifier name
     */
    @Bean(name = "SegmentIdGenerator")
    IDGen segmentIdGenerator(DataSource dataSource) {
        if (!properties.getSegment().isEnabled()) {
            log.warn("Zero ID Gen generator init successfully for segment.");
            return new ZeroIDGen();
        }

        // Config DAO
        IDAllocDao dao = new IDAllocDaoImpl(dataSource);

        // Config Id Generator
        SegmentIDGenImpl segmentIdGenerator = new SegmentIDGenImpl();
        segmentIdGenerator.setDao(dao);

        return segmentIdGenerator;
    }

    /**
     * Snowflake id服务配置
     *
     * @return {@link IDGen} generator with qualifier name
     */
    @Bean(name = "SnowflakeIdGenerator")
    IDGen snowflakeIdGenerator() {
        if (!properties.getSnowflake().isEnabled()) {
            log.warn("Zero ID Gen generator init successfully for Snowflake.");
            return new ZeroIDGen();
        }

        IdGeneratorConfigurationProperties.ZookeeperProperties zk = properties.getSnowflake().getZookeeper();
        return new SnowflakeIDGenImpl(zk.getHost(), zk.getPort());
    }
}
