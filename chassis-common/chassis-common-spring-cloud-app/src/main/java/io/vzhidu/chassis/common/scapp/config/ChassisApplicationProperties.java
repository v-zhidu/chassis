package io.vzhidu.chassis.common.scapp.config;

import io.vzhidu.chassis.common.core.util.Preconditions;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

/**
 * Chassis application configuration properties definition
 *
 * @author Zhiqiang Du Created at 2020/12/2
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "chassis.app")
@Getter
@ToString
public class ChassisApplicationProperties {


    /**
     * Enable development or not, default is true
     */
    private final boolean devMode;

    /**
     * Application name, default is application
     */
    private final String name;

    /**
     * Application listening port, default is 8080
     */
    private final int port;

    /**
     * Unique id for this service instance.
     */
    private final String instanceId;

    /**
     * Group for this service instance, default is development
     */
    private final String instanceGroup;

    /**
     * Tags for the service instance, default is empty
     */
    private final List<String> tags;

    /**
     * Constructor
     *
     * @param devMode       development or not
     * @param name          application name
     * @param port          application listening port
     * @param instanceId    unique id for this service instance
     * @param instanceGroup group for this service instance
     */
    public ChassisApplicationProperties(boolean devMode, String name, int port,
                                        String instanceId, String instanceGroup, List<String> tags) {
        Preconditions.checkNotNull(name, "[chassis.app.name] must not be null");

        this.devMode = devMode;
        this.name = name;
        this.port = port;
        this.instanceId = instanceId;
        this.instanceGroup = instanceGroup;
        this.tags = tags;
    }
}
