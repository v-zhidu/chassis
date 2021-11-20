package io.vzhidu.chassis.middleware.idgenerator.server.controller;

import io.vzhidu.chassis.common.core.rpc.Response;
import io.vzhidu.chassis.middleware.idgenerator.core.BizTag;
import io.vzhidu.chassis.middleware.idgenerator.core.BizTagDetail;
import io.vzhidu.chassis.middleware.idgenerator.core.IdMonitor;
import io.vzhidu.chassis.middleware.idgenerator.core.SnowflakeContent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The restful APIs of Identifier monitor service.
 *
 * @author Zhiqiang Du Created at 2020/8/21
 */
@RestController
public class IdMonitorController {

    /**
     * Interface of {@link IdMonitor}
     */
    private final IdMonitor idMonitor;

    /**
     * Default Constructor
     *
     * @param idMonitor see {@link IdMonitor}
     */
    public IdMonitorController(IdMonitor idMonitor) {
        this.idMonitor = idMonitor;
    }

    /**
     * 获取所有业务号段基础信息
     *
     * @return 所有业务号段列表, 或空列表
     */
    @GetMapping("/api/segments")
    public Response<List<BizTag>> getAllBizTags() {
        return Response.ok(idMonitor.getAllBizTags());
    }

    /**
     * 根据业务号段代码获取当前使用状态
     *
     * @param key 业务号段代码
     * @return xxx
     */
    @GetMapping("/api/segments/{key}")
    public Response<BizTagDetail> getBizTagDetail(@PathVariable("key") String key) {
        return Response.ok(idMonitor.getBizTag(key));
    }

    /**
     * GET /ids/v1/snowflakes/decode 解析snowflake id
     *
     * @param idStr snowflake id
     * @return 解析结果
     */
    @GetMapping("/api/snowflakes/{id}")
    public Response<SnowflakeContent> getSnowflakeIdDetail(@PathVariable("id") String idStr) {
        long snowflakeId = Long.parseLong(idStr);
        return Response.ok(idMonitor.decodeSnowflakeId(snowflakeId));
    }
}
