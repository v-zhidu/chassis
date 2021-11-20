package io.vzhidu.chassis.middleware.idgenerator.server.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * The configuration properties for ID Generator
 *
 * @author Zhiqiang Du Created at 2020/11/24
 */
@Getter
@ConstructorBinding
@ConfigurationProperties("middleware.id-generator")
public class IdGeneratorConfigurationProperties {

    /**
     * Segment ID generator properties
     */
    private final SegmentProperties segment;

    /**
     * Snowflake ID generator properties
     */
    private final SnowflakeProperties snowflake;

    /**
     * Constructor for {@link IdGeneratorConfigurationProperties}
     *
     * @param segment see {@link SegmentProperties}
     * @param snowflake see {@link SnowflakeProperties}
     */
    public IdGeneratorConfigurationProperties(SegmentProperties segment, SnowflakeProperties snowflake) {
        this.segment = segment;
        this.snowflake = snowflake;
    }

    /**
     * Segment ID generator properties
     */
    @Getter
    public static class SegmentProperties {

        /**
         * Enable segment ID generator or not
         */
        private final boolean enabled;

        /**
         * Constructor for {@link SegmentProperties}
         *
         * @param enabled enable or not
         */
        public SegmentProperties(boolean enabled) {
            this.enabled = enabled;
        }
    }

    /**
     * Snowflake ID generator properties
     */
    @Getter
    public static class SnowflakeProperties {

        /**
         * Enable snowflake ID generator or not
         */
        private final boolean enabled;

        /**
         * Zookeeper setting for Snowflake ID generator
         */
        private final ZookeeperProperties zookeeper;

        /**
         * Constructor for {@link SnowflakeProperties}
         *
         * @param enabled enable or not
         * @param zookeeper see {@link ZookeeperProperties}
         */
        public SnowflakeProperties(boolean enabled, ZookeeperProperties zookeeper) {
            this.enabled = enabled;
            this.zookeeper = zookeeper;
        }
    }

    /**
     * Zookeeper properties
     */
    @Getter
    public static class ZookeeperProperties {

        /**
         * The host of zookeeper server
         */
        private final String host;

        /**
         * The port of zookeeper server
         */
        private final int port;

        /**
         * Constructor for {@link ZookeeperProperties}
         *
         * @param host the host of zookeeper server
         * @param port the port of zookeeper server
         */
        public ZookeeperProperties(@DefaultValue("127.0.0.1") String host, @DefaultValue("2181") int port) {
            this.host = host;
            this.port = port;
        }
    }
}
