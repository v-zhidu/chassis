package io.vzhidu.chassis.common.scof.feign;

import feign.Logger;
import feign.Request;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 将原有的debug级别，修改成info级别
 *
 * @author Zhiqiang Du created at 2021/5/13
 */
@Slf4j
public class InfoSlf4jLogger extends Logger {

    private final org.slf4j.Logger logger;

    public InfoSlf4jLogger() {
        this(feign.Logger.class);
    }

    public InfoSlf4jLogger(Class<?> clazz) {
        this(LoggerFactory.getLogger(clazz));
    }

    public InfoSlf4jLogger(String name) {
        this(LoggerFactory.getLogger(name));
    }

    public InfoSlf4jLogger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    /**
     * override default implementation of log request and response
     */
    @Override
    protected Response logAndRebufferResponse(String configKey,
                                              Level logLevel,
                                              Response response,
                                              long elapsedTime)
            throws IOException {
        if (logger.isInfoEnabled()) {
            return super.logAndRebufferResponse(configKey, logLevel, response, elapsedTime);
        }
        return response;
    }

    /**
     * override default implementation of log request and response
     */
    @Override
    protected void log(String configKey, String format, Object... args) {
        // Not using SLF4J's support for parameterized messages (even though it would be more efficient)
        // because it would require the incoming message formats to be SLF4J-specific.
        if (logger.isInfoEnabled()) {
            logger.info(methodTag(configKey) + String.format(format, args));
        }
    }
}
