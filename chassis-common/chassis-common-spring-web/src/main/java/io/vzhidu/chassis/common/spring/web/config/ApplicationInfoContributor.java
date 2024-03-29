package io.vzhidu.chassis.common.spring.web.config;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

/**
 * Adding application information to actuator info endpoint
 *
 * @author Zhiqiang Du created at 2021/9/9
 */
@Component
public class ApplicationInfoContributor implements InfoContributor {

    private final ApplicationInformationProperties properties;

    public ApplicationInfoContributor(ApplicationInformationProperties properties) {
        this.properties = properties;
    }

    /**
     * Adding application information to actuator info endpoint
     */
    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("app", properties);
    }
}
