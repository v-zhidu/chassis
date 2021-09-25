package io.vzhidu.chassis.common.scof.feign;

import feign.Experimental;
import feign.Response;
import feign.codec.ErrorDecoder;

/**
 * 拦截非2xx返回的response，并翻译为内部服务的异常继续传播
 *
 * @author Zhiqiang Du created at 2021/5/13
 */
@Experimental
public class ScappErrorDecoder extends ErrorDecoder.Default {

    /**
     * 未实现
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        return super.decode(methodKey, response);
    }
}
