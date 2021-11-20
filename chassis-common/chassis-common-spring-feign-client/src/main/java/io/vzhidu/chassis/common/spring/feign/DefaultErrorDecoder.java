package io.vzhidu.chassis.common.spring.feign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import io.vzhidu.chassis.common.core.ex.InternalException;
import io.vzhidu.chassis.common.core.rpc.RpcError;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认的错误处理方法
 *
 * @author Zhiqiang Du created at 2021/10/12
 */
@Slf4j
public class DefaultErrorDecoder extends ErrorDecoder.Default {

    private final ObjectMapper objectMapper;

    public DefaultErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Implement this method in order to decode an HTTP {@link Response} when
     * {@link Response#status()} is not in the 2xx range. Please raise application-specific exceptions
     * where possible. If your exception is retryable, wrap or subclass {@link RetryableException}
     *
     * @param methodKey {@link Feign#configKey} of the java method that invoked the request. ex.
     *                  {@code IAM#getUser()}
     * @param response  HTTP response where {@link Response#status() status} is greater than or equal
     *                  to {@code 300}.
     * @return Exception IOException, if there was a network error reading the response or an
     * application-specific exception decoded by the implementation. If the throwable is
     * retryable, it should be wrapped, or a subtype of {@link RetryableException}
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        log.debug("Caught Feign client error, method: {}, status: {} {}",
                methodKey, response.status(), response.reason());

        FeignException exception = FeignException.errorStatus(methodKey, response);

        io.vzhidu.chassis.common.core.rpc.Response<?> rpcResponse;
        try {
            rpcResponse = objectMapper.readValue(exception.contentUTF8(),
                    io.vzhidu.chassis.common.core.rpc.Response.class);

            if (rpcResponse == null || rpcResponse.getStatus() == null) {
                return super.decode(methodKey, response);
            }

            if (rpcResponse.isOk()) {
                log.error("Not found errors in [Response], but still caught by feign", exception);
                return new InternalException("Not found errors in [Response], but still caught by feign", exception);
            }

            RpcError error = rpcResponse.getStatus().getError();

            return RpcError.translateTo(error, exception);
        } catch (JsonProcessingException ex) {
            // 返回数据结构非标准结构，返回原始异常
            return super.decode(methodKey, response);
        }
    }
}
