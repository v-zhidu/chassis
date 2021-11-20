package io.vzhidu.chassis.middleware.idgenerator.core;

import io.vzhidu.chassis.common.core.ex.InvalidArgumentException;
import io.vzhidu.chassis.common.core.ex.NotFoundException;

import java.util.List;

/**
 * IDGenerator 监控
 *
 * @author Zhiqiang Du Created at 2020/3/18
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
     * @throws NotFoundException 找不到key
     */
    BizTagDetail getBizTag(String key);

    /**
     * 解析Snowflake id
     * <p></p>
     *
     * @param snowflakeId string content of Snowflake id
     * @return {@link SnowflakeContent}
     * @throws InvalidArgumentException idStr为格式化错误
     */
    SnowflakeContent decodeSnowflakeId(long snowflakeId);
}

