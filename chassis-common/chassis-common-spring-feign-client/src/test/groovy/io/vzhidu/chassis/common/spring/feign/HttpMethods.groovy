package io.vzhidu.chassis.common.spring.feign

import feign.Response
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.SpringQueryMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

import javax.validation.Valid
/**
 * Testing different HTTP verbs
 *
 * @author Zhiqiang Du created at 2021/9/16
 */
@FeignClient(name = "test-http-method", configuration = HttpbinFeignClientConfiguration.class)
interface HttpMethods extends Httpbin {

    /**
     * Test GET method with query parameters.
     * Sample url: https://httpbin.org/get
     *
     * @param parameter the kv pairs of parameters
     * @return see {@link HttpbinResponse}
     */
    @GetMapping("/get")
    HttpbinResponse get(@SpringQueryMap Map<String, Object> parameter);

    /**
     * Test POST method with object.
     * Sample url: https://httpbin.org/get
     *
     * @param obj the data you post
     * @return {@link HttpbinResponse}
     */
    @PostMapping("/post")
    HttpbinResponse post(Object obj);

    /**
     * Test GET method with query parameters.
     * Sample url: https://httpbin.org/get
     *
     * @param parameter the kv pairs of parameters
     * @return see {@link HttpbinResponse}
     */
    @GetMapping("/get")
    Response getOriginalResponse(@SpringQueryMap Map<String, Object> parameter);

    /**
     * Test POST method with object.
     * Sample url: https://httpbin.org/get
     *
     * @param request the data you post
     * @return {@link HttpbinResponse}
     */
    @Valid
    @PostMapping("/post")
    HttpbinResponse postValidate(@Valid HttpbinRequest request);

}
