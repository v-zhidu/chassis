package io.vzhidu.chassis.middleware.idgenerator.client;

import io.vzhidu.chassis.middleware.idgenerator.core.api.IdGeneratorEndpoint;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Feign client for id-identifier service
 *
 * @author Zhiqiang Du Created at 2020/3/24
 */
@FeignClient(name = "chassis-middleware-id-generator-server")
interface IdGeneratorFeignClient extends IdGeneratorEndpoint {
}
