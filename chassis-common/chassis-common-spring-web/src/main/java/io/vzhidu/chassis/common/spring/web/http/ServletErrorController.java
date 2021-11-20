package io.vzhidu.chassis.common.spring.web.http;

import io.vzhidu.chassis.common.core.ex.NotFoundException;
import io.vzhidu.chassis.common.core.rpc.RpcError;
import io.vzhidu.chassis.common.core.rpc.RpcStatus;
import io.vzhidu.chassis.common.spring.web.config.ApplicationInformationProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.ActiveSpan;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generic Global Error Controller
 *
 * @author Zhiqiang Du Created at 2019/9/27
 */
@Slf4j
@RestController
public class ServletErrorController extends BasicErrorController {

    private static final String RESOURCE_NOT_FOUND_MESSAGE_FORMAT =
            "Resource [%s] you requested is not found";

    private final ApplicationInformationProperties applicationProperties;

    public ServletErrorController(ApplicationInformationProperties applicationProperties,
                                  ErrorAttributes errorAttributes,
                                  ServerProperties serverProperties) {
        super(errorAttributes, serverProperties.getError());
        this.applicationProperties = applicationProperties;
    }

    /**
     * 覆盖默认的错误信息
     */
    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> attributes = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        RpcStatus rpcStatus = translateToRpcStatus(attributes, request);

        Map<String, Object> body = new HashMap<>(1);
        body.put("status", rpcStatus);
        return new ResponseEntity<>(body, getStatus(request));
    }

    private RpcStatus translateToRpcStatus(Map<String, Object> attributes, HttpServletRequest request) {
        Object statusObj = attributes.get("status");
        HttpStatus status;
        if (statusObj == null) {
            status = getStatus(request);
        } else {
            status = HttpStatus.valueOf((Integer) statusObj);
        }

        RpcStatus rpcStatus;
        switch (status) {
            case NOT_FOUND:
                rpcStatus = translateNotFoundError(attributes);
                break;
            case INTERNAL_SERVER_ERROR:
                rpcStatus = translateInternalError(attributes);
                break;
            default:
                rpcStatus = RpcStatus.UNKNOWN;
                break;
        }

        return rpcStatus;
    }

    private RpcStatus translateNotFoundError(Map<String, Object> attributes) {
        RpcError.Builder builder = initRpcErrorBuilder(attributes);

        String message = String.format(RESOURCE_NOT_FOUND_MESSAGE_FORMAT, attributes.get("path"));
        NotFoundException ex = new NotFoundException(message);
        if (getErrorProperties().isIncludeException()) {
            builder.exception(ex);
        }

        ActiveSpan.error(ex);
        return RpcStatus.NOT_FOUND.withError(builder.build());
    }

    private RpcStatus translateInternalError(Map<String, Object> attributes) {
        RpcError.Builder builder = initRpcErrorBuilder(attributes);

        final String message = (String) attributes.get("message");
        final String exception = (String) attributes.get("exception");
        if (getErrorProperties().isIncludeException()) {
            builder.exception(message, exception);
        }

        ActiveSpan.error(exception);
        return RpcStatus.INTERNAL.withError(builder.build());
    }

    private RpcError.Builder initRpcErrorBuilder(Map<String, Object> attributes) {
        return RpcError.builder()
                .timestamp(parseTimestamp((Date) attributes.get("timestamp")))
                .metadata(
                        TraceContext.traceId(),
                        applicationProperties.getName(),
                        (String) attributes.get("path")
                );
    }

    private static String parseTimestamp(Date date) {
        return String.valueOf(date.toInstant().toEpochMilli());
    }
}
