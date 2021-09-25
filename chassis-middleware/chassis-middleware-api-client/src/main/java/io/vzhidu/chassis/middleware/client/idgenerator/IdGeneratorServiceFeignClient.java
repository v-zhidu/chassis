package io.vzhidu.chassis.middleware.client.idgenerator;

import io.vzhidu.chassis.common.core.rpc.Response;
import io.vzhidu.chassis.common.scof.FeignClientCustomizeConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for id-identifier service
 *
 * @author Zhiqiang Du Created at 2020/3/24
 */
@FeignClient(name = "chassis-middleware-id-generator", configuration = FeignClientCustomizeConfiguration.class)
public interface IdGeneratorServiceFeignClient {

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
