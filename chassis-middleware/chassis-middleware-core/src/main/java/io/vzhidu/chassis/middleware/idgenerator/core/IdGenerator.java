package io.vzhidu.chassis.middleware.idgenerator.core;

import io.vzhidu.chassis.common.core.ex.NotFoundException;
import io.vzhidu.chassis.common.core.ex.UnknownException;

/**
 * 分布式id生成服务
 * <p></p>
 *
 * @author Zhiqiang Du Created at 2020/3/18
 */
public interface IdGenerator {

    /**
     * 获取号段id
     * <p></p>
     *
     * @param key 业务代码
     * @return 全局唯一的号段id
     * @throws NotFoundException 找不到key
     * @throws UnknownException  未知异常
     */
    long getSegmentId(String key);

    /**
     * 获取Snowflake类型id
     * <p></p>
     *
     * @param key 业务代码
     * @return 全局唯一的id
     * @throws NotFoundException 找不到key
     * @throws UnknownException  未知异常
     */
    long getSnowflakeId(String key);
}
