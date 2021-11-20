package io.vzhidu.chassis.middleware.idgenerator.server.controller;

import io.vzhidu.chassis.common.core.rpc.Response;
import io.vzhidu.chassis.middleware.idgenerator.core.IdGenerator;
import io.vzhidu.chassis.middleware.idgenerator.core.api.IdGeneratorEndpoint;
import org.springframework.web.bind.annotation.RestController;

/**
 * The restful APIs of Identifier service.
 *
 * @author Zhiqiang Du Created at 2020/8/21
 */
@RestController
public class IdGeneratorController implements IdGeneratorEndpoint {

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
     */
    @Override
    public Response<String> getSegmentId(String key) {
        return Response.ok(String.valueOf(idGenerator.getSegmentId(key)));
    }

    /**
     * 获取Snowflake类型id
     * <p></p>
     *
     * @param key 业务代码
     * @return 全局唯一的id
     */
    @Override
    public Response<String> getSnowflakeId(String key) {
        return Response.ok(String.valueOf(idGenerator.getSnowflakeId(key)));
    }
}
