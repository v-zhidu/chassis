package io.vzhidu.chassis.middleware.core.idgenerator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Snowflake解析后的内容, 例如：
 * {
 * "id": "1385475468735549487",
 * "timestamp": "1567733700834(2019-09-06 09:35:00.834)",
 * "sequenceId": "3448",
 * "workerId": "39"
 * }
 *
 * @author Zhiqiang Du Created at 2020/8/21
 */
@Getter
@Setter
@ToString
public class SnowflakeContent {

    /**
     * Snowflake id
     */
    private String id;

    /**
     * Timestamp
     */
    private String timestamp;

    /**
     * Sequence id
     */
    private long sequenceId;

    /**
     * Instance worker id
     */
    private long workerId;
}
