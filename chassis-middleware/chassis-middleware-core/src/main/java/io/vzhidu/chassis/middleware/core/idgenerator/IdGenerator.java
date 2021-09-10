package io.vzhidu.chassis.middleware.core.idgenerator;

import io.vzhidu.chassis.middleware.core.idgenerator.ex.IdGenerateException;
import io.vzhidu.chassis.middleware.core.idgenerator.ex.NoSuchKeyException;

import javax.annotation.Nonnull;

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
     * @throws NoSuchKeyException  key不存在或非法
     * @throws IdGenerateException 生成id遇到未知错误
     */
    long getSegmentId(@Nonnull String key);

    /**
     * 获取Snowflake类型id
     * <p></p>
     *
     * @param key 业务代码
     * @return 全局唯一的id
     * @throws NoSuchKeyException  key不存在或非法
     * @throws IdGenerateException 生成id遇到未知错误
     */
    long getSnowflakeId(@Nonnull String key);
}
