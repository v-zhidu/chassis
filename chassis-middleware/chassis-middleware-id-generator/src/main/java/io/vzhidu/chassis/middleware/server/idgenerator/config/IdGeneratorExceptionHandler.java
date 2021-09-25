package io.vzhidu.chassis.middleware.server.idgenerator.config;

import io.vzhidu.chassis.common.core.rpc.Response;
import io.vzhidu.chassis.common.core.rpc.RpcStatus;
import io.vzhidu.chassis.common.scapp.http.ClientErrorHandler;
import io.vzhidu.chassis.middleware.core.idgenerator.ex.IdGenerateException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * 自定义全局异常处理
 *
 * @author Zhiqiang Du Created at 2020/9/3
 */
@ControllerAdvice
public class IdGeneratorExceptionHandler extends ClientErrorHandler {

    /**
     * 处理{@link IdGenerateException}
     *
     * @param ex the ex to get its information
     * @return the http response
     */
    @ResponseBody
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler({IdGenerateException.class})
    public Response<Void> handleIdentifierGeneratorServerException(HttpServletRequest request, Exception ex) {
        return createHttpErrorInfo(RpcStatus.INTERNAL, request, ex);
    }
}
