package io.vzhidu.chassis.common.spring.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;

/**
 * 拦截请求，添加默认的请求头信息
 *
 * @author Zhiqiang Du created at 2021/10/11
 */
public class HeadersRequestInterceptor implements RequestInterceptor {

    /**
     * 添加默认的请求头信息
     *
     * @param template {@link RequestTemplate}
     */
    @Override
    public void apply(RequestTemplate template) {
        template.header("X-Trace-Id", "gt_" + TraceContext.traceId());
        template.header("Content-Type", "application/json");
    }
}
