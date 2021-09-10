package io.vzhidu.chassis.common.scapp.http;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.vzhidu.chassis.common.core.ex.InvalidParameterException;
import io.vzhidu.chassis.common.core.ex.PermissionDeniedException;
import io.vzhidu.chassis.common.core.ex.ResourceNotFoundException;
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

import javax.servlet.http.HttpServletRequest;
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
    public Response<?> handleInvalidInputException(HttpServletRequest request, Exception ex) {
        return createHttpErrorInfo(RpcStatus.INVALID_ARGUMENT, request, new InvalidParameterException(ex.getMessage()));
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
    public Response<?> handleHttpMessageNotReadableException(HttpServletRequest request,
                                                             HttpMessageNotReadableException ex) {
        Throwable t = ex.getCause();
        if (t instanceof InvalidFormatException) {
            final InvalidFormatException invalidFormatException = (InvalidFormatException) t;
            final String message;
            if (invalidFormatException.getTargetType().isEnum()) {
                message = String.format("Value [%s] is not in values %s", invalidFormatException.getValue().toString(),
                        Arrays.toString(invalidFormatException.getTargetType().getEnumConstants()));
            } else {
                message = String.format("Value [%s] is invalid", invalidFormatException.getValue().toString());
            }

            return createHttpErrorInfo(RpcStatus.INVALID_ARGUMENT, request, new InvalidParameterException(message));
        } else {
            final String message = "Required request body is missing";
            return createHttpErrorInfo(RpcStatus.INVALID_ARGUMENT, request, new InvalidParameterException(message));
        }
    }

    /**
     * 处理{@link InvalidParameterException}
     *
     * @param request the request to get some request information
     * @param ex      the ex to get its information
     * @return the http response
     */
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = {InvalidParameterException.class})
    public Response<?> handleInvalidParameterException(HttpServletRequest request, InvalidParameterException ex) {
        return createHttpErrorInfo(RpcStatus.INVALID_ARGUMENT, request, ex);
    }

    /**
     * 处理{@link PermissionDeniedException}
     *
     * @param request the request to get some request information
     * @param ex      the ex to get its information
     * @return the http response
     */
    @ResponseBody
    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(value = PermissionDeniedException.class)
    public Response<?> handlePermissionDeniedException(HttpServletRequest request, PermissionDeniedException ex) {
        return createHttpErrorInfo(RpcStatus.PERMISSION_DENIED, request, ex);
    }

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
    public Response<?> handleRequestMethodNotSupportedException(HttpServletRequest request,
                                                                HttpRequestMethodNotSupportedException ex) {
        return createHttpErrorInfo(RpcStatus.NOT_FOUND, request, new ResourceNotFoundException(ex.getMessage()));
    }

    /**
     * {@link ResourceNotFoundException}
     *
     * @param request the request to get some request information
     * @param ex      the ex to get its information
     * @return the http response
     */
    @ResponseBody
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public Response<?> handleNotFoundExceptions(HttpServletRequest request, ResourceNotFoundException ex) {
        return createHttpErrorInfo(RpcStatus.NOT_FOUND, request, ex);
    }

    /**
     * 处理参数异常
     *
     * @param t the exception class
     * @return {@link Response}
     */
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    public Response<?> handleBindingResultException(HttpServletRequest request, Throwable t) {
        BindingResult bindingResult = extractBindingResult(t);
        FieldError error = bindingResult.getFieldError();
        assert error != null;

        final String message = String.format("Field [%s] is invalid, %s", error.getField(),
                error.getDefaultMessage());
        return createHttpErrorInfo(RpcStatus.INVALID_ARGUMENT, request, new InvalidParameterException(message));

    }

    private BindingResult extractBindingResult(Throwable error) {
        if (error instanceof MethodArgumentNotValidException) {
            return ((MethodArgumentNotValidException) error).getBindingResult();
        }

        return (BindingResult) error;
    }

    /**
     * 处理异常封装后的http response
     *
     * @param status  RPC response status
     * @param request {@link HttpServletRequest}
     * @param ex      异常
     * @return the response wrapped by {@link Response}
     */
    protected Response<?> createHttpErrorInfo(RpcStatus status, HttpServletRequest request, Exception ex) {
        ActiveSpan.error(ex);
        String traceId = TraceContext.traceId();

        final String path = request.getServletPath();

        RpcError error = RpcError.builder()
                .timestamp(String.valueOf(Instant.now().toEpochMilli()))
                .metadata(traceId, environment.getProperty("chassis.app.name"), path)
                .exception(ex)
                .build();
        status = status.withError(error);
        log.warn("Returning code: {} for path: {}", status, path);
        return Response.fail(status.withError(error));
    }
}
