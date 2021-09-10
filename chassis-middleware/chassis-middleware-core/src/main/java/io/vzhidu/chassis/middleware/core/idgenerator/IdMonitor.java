package io.vzhidu.chassis.middleware.core.idgenerator;

import io.vzhidu.chassis.middleware.core.idgenerator.ex.IllegalSnowflakeIdFormatException;
import io.vzhidu.chassis.middleware.core.idgenerator.ex.NoSuchKeyException;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * IDGenerator 监控
 */
public interface IdMonitor {

    /**
     * 获取所有业务号段
     *
     * @return 业务号段列表
     */
    List<BizTag> getAllBizTags();

    /**
     * 获取业务号段详细信息
     *
     * @param key 业务代码
     * @return {@link BizTagDetail}
     * @throws NoSuchKeyException key不存在或非法
     */
    BizTagDetail getBizTag(@Nonnull String key);

    /**
     * 解析Snowflake id
     * <p></p>
     *
     * @param snowflakeId string content of Snowflake id
     * @return {@link SnowflakeContent}
     * @throws IllegalSnowflakeIdFormatException idStr为格式化错误
     */
    SnowflakeContent decodeSnowflakeId(@Nonnull long snowflakeId);
}
