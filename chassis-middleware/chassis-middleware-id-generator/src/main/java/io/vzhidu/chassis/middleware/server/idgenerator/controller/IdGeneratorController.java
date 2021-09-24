package io.vzhidu.chassis.middleware.server.idgenerator.controller;

import io.vzhidu.chassis.common.core.rpc.Response;
import io.vzhidu.chassis.middleware.core.idgenerator.IdGenerator;
import io.vzhidu.chassis.middleware.core.idgenerator.ex.IdGenerateException;
import io.vzhidu.chassis.middleware.core.idgenerator.ex.NoSuchKeyException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * The restful APIs of Identifier service.
 *
 * @author Zhiqiang Du Created at 2020/8/21
 */
@RestController
public class IdGeneratorController {

    /**
     * Interface of {@link IdGenerator}
     */
    private final IdGenerator idGenerator;

    /**
     * Default constructor
     *
     * @param idGenerator object implementation {@link IdGenerator}
     */
    public IdGeneratorController(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    /**
     * 获取号段id
     *
     * @param key 业务号段代码
     * @return 返回唯一id
     * @throws NoSuchKeyException  key不存在或非法
     * @throws IdGenerateException 生成id遇到未知错误
     */
    @GetMapping("/api/segments/{key}/ids")
    public Response<String> getSegmentId(@PathVariable("key") String key) {
        return Response.ok(String.valueOf(idGenerator.getSegmentId(key)));
    }

    /**
     * 获取Snowflake类型id
     * <p></p>
     *
     * @param key 业务代码
     * @return 全局唯一的id
     * @throws NoSuchKeyException  key不存在或非法
     * @throws IdGenerateException 生成id遇到未知错误
     */
    @GetMapping("/api/snowflakes/{key}/ids")
    public Response<String> getSnowflakeId(@PathVariable("key") String key) {
        return Response.ok(String.valueOf(idGenerator.getSnowflakeId(key)));
    }
}
