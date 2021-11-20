package io.vzhidu.chassis.middleware.idgenerator.core.api;

import io.vzhidu.chassis.common.core.rpc.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 分布式id接口声明
 *
 * @author Zhiqiang Du created at 2021/10/11
 */
public interface IdGeneratorEndpoint {

    /**
     * GET /api/segment/get 获取全局唯一id
     *
     * @param key biz key
     * @return 获取全局唯一id
     */
    @GetMapping("/api/segments/{key}/ids")
    Response<String> getSegmentId(@PathVariable("key") String key);

    /**
     * GET /api/snowflake/get 获取全局唯一id
     *
     * @param key biz key
     * @return 获取全局唯一id
     */
    @GetMapping("/api/snowflakes/{key}/ids")
    Response<String> getSnowflakeId(@PathVariable("key") String key);
}
