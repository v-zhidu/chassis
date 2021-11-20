package io.vzhidu.chassis.common.spring.web.http;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.vzhidu.chassis.common.core.ex.ClientException;
import io.vzhidu.chassis.common.core.ex.InvalidArgumentException;
import io.vzhidu.chassis.common.core.ex.NotFoundException;
import io.vzhidu.chassis.common.core.ex.ServerException;
import io.vzhidu.chassis.common.core.rpc.Response;
import io.vzhidu.chassis.common.core.rpc.RpcError;
import io.vzhidu.chassis.common.core.rpc.RpcStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.ActiveSpan;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Arrays;

import static org.springframework.http.HttpStatus.*;

/**
 * 通用全局异常处理器
 *
 * @author Zhiqiang Du Created at 2019/9/27
 */
@Slf4j
@RestControllerAdvice
public class ClientErrorHandler implements EnvironmentAware {

    private Environment environment;

    /**
     * implementation of {@link EnvironmentAware#setEnvironment(Environment)}
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    //region 400 Bad Request

    /**
     * {@link HttpMediaTypeNotSupportedException}
     * {@link MissingServletRequestParameterException}
     *
     * @param request the request to get some request information
     * @param ex      the ex to get its information
     * @return the http response
     */
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({
            HttpMediaTypeNotSupportedException.class,
            MissingServletRequestParameterException.class
    })
    public Response<Void> handleInvalidInputException(HttpServletRequest request, Exception ex) {
        return createHttpErrorInfo(RpcStatus.INVALID_ARGUMENT, request,
                new InvalidArgumentException(ex.getMessage(), ex));
    }

    /**
     * {@link HttpMessageNotReadableException}
     *
     * @param request the request to get some request information
     * @param ex      the ex to get its information
     * @return the http response
     */
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response<Void> handleHttpMessageNotReadableException(HttpServletRequest request,
                                                                HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {
            final InvalidFormatException invalidFormatException = (InvalidFormatException) cause;
            final String message;
            if (invalidFormatException.getTargetType().isEnum()) {
                message = String.format("Value [%s] is not in values %s", invalidFormatException.getValue().toString(),
                        Arrays.toString(invalidFormatException.getTargetType().getEnumConstants()));
            } else {
                message = String.format("Value [%s] is invalid", invalidFormatException.getValue().toString());
            }

            return createHttpErrorInfo(RpcStatus.INVALID_ARGUMENT, request, new InvalidArgumentException(message, ex));
        } else {
            final String message = "Required request body is missing";
            return createHttpErrorInfo(RpcStatus.INVALID_ARGUMENT, request, new InvalidArgumentException(message, ex));
        }
    }

    /**
     * 处理参数类型异常 {@link MethodArgumentTypeMismatchException}
     *
     * @param ex the exception class
     * @return {@link Response}
     */
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public Response<Void> handleMethodArgumentTypeMismatchException(HttpServletRequest request,
                                                                    MethodArgumentTypeMismatchException ex) {
        final String message = String.format("Field [%s] is invalid, value: %s", ex.getName(), ex.getValue());
        return createHttpErrorInfo(RpcStatus.INVALID_ARGUMENT, request, new InvalidArgumentException(message, ex));
    }

    /**
     * 处理参数异常
     *
     * @param cause the exception class
     * @return {@link Response}
     */
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = {
            BindException.class,
            MethodArgumentNotValidException.class
    })
    public Response<Void> handleBindingResultException(HttpServletRequest request, Throwable cause) {
        BindingResult bindingResult = extractBindingResult(cause);
        FieldError error = bindingResult.getFieldError();
        assert error != null;

        final String message = String.format("Field [%s] is invalid, %s", error.getField(), error.getDefaultMessage());
        return createHttpErrorInfo(RpcStatus.INVALID_ARGUMENT, request, new InvalidArgumentException(message, cause));

    }

    private BindingResult extractBindingResult(Throwable error) {
        if (error instanceof MethodArgumentNotValidException) {
            return ((MethodArgumentNotValidException) error).getBindingResult();
        }

        return (BindingResult) error;
    }

    //endregion

    //region 404 Not Found

    /**
     * {@link HttpRequestMethodNotSupportedException}
     *
     * @param request the request to get some request information
     * @param ex      the ex to get its information
     * @return the http response
     */
    @ResponseBody
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response<Void> handleRequestMethodNotSupportedException(HttpServletRequest request,
                                                                   HttpRequestMethodNotSupportedException ex) {
        return createHttpErrorInfo(RpcStatus.NOT_FOUND, request, new NotFoundException(ex.getMessage(), ex));
    }

    //endregion

    //region 自定义异常处理

    /**
     * 处理{@link ClientException}
     *
     * @param request the request to get some request information
     * @param ex      the ex to get its information
     * @return the http response
     */
    @ResponseBody
    @ExceptionHandler(value = {ClientException.class})
    public Response<Void> handleClientException(HttpServletRequest request,
                                                HttpServletResponse response,
                                                ClientException ex) {
        response.setStatus(ex.getHttpStatus().value());
        return createHttpErrorInfo(ex.getRpcStatus(), request, ex);
    }

    /**
     * 处理{@link ServerException}
     *
     * @param request the request to get some request information
     * @param ex      the ex to get its information
     * @return the http response
     */
    @ResponseBody
    @ExceptionHandler(value = {ServerException.class})
    public Response<Void> handleServerException(HttpServletRequest request,
                                                HttpServletResponse response,
                                                ServerException ex) {
        // logging the server exception
        log.error(ex.getMessage(), ex);
        response.setStatus(ex.getHttpStatus().value());
        return createHttpErrorInfo(ex.getRpcStatus(), request, ex);
    }

    //endregion

    /**
     * 处理异常封装后的http response
     *
     * @param status  RPC response status
     * @param request {@link HttpServletRequest}
     * @param cause   异常
     * @return the response wrapped by {@link Response}
     */
    protected Response<Void> createHttpErrorInfo(RpcStatus status, HttpServletRequest request, Throwable cause) {
        ActiveSpan.error(cause);
        String traceId = TraceContext.traceId();

        final String path = request.getServletPath();

        RpcError error = RpcError.builder()
                .timestamp(String.valueOf(Instant.now().toEpochMilli()))
                .metadata(traceId, environment.getProperty("app.name"), path)
                .exception((Exception) cause)
                .build();
        status = status.withError(error);
        log.warn("Returning code: {} for path: {}", status, path);
        return Response.fail(status.withError(error));
    }
}
