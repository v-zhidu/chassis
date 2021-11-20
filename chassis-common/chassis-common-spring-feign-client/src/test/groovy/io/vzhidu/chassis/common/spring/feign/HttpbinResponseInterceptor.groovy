package io.vzhidu.chassis.common.spring.feign

import okhttp3.Interceptor
import okhttp3.Response

import java.time.Instant

/**
 * Response interceptor for {@link HttpMethods#postValidate(HttpbinRequest)}
 *
 * @author Zhiqiang Du created at 2021/10/5
 */
class HttpbinResponseInterceptor implements Interceptor {

    /**
     * Intercept response and add x-timestamp header to response.
     */
    @Override
    Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request())
        return originalResponse.newBuilder().header("X-Timestamp", Instant.now().toString()).build()
    }
}

