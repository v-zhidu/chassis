package io.vzhidu.chassis.common.spring.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vzhidu.chassis.common.core.ex.InvalidArgumentException;
import io.vzhidu.chassis.common.core.ex.NotFoundException;
import io.vzhidu.chassis.common.core.ex.PermissionDeniedException;
import io.vzhidu.chassis.common.core.rpc.Response;
import io.vzhidu.chassis.common.core.rpc.RpcError;
import io.vzhidu.chassis.common.core.rpc.RpcStatus;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponseAssertion {

    public static final Response<?> EXCEPTION = Response.fail(
            RpcStatus.INTERNAL
                    .withError(
                            RpcError.builder()
                                    .metadata("", "application", "/mock/ex")
                                    .exception(new Exception("unexpect exception"))
                                    .build()
                    )
    );

    public static final Response<?> MEDIA_TYPE_EXCEPTION = Response.fail(
            RpcStatus.INVALID_ARGUMENT
                    .withError(
                            RpcError.builder()
                                    .metadata("", "application", "/mock/post")
                                    .exception(new InvalidArgumentException("Content type 'text/plain;charset=UTF-8' not supported"))
                                    .build()
                    )
    );

    public static final Response<?> MISSING_REQUEST_PARAMETER_EXCEPTION = Response.fail(
            RpcStatus.INVALID_ARGUMENT
                    .withError(
                            RpcError.builder()
                                    .metadata("", "application", "/mock/get")
                                    .exception(new InvalidArgumentException("Required String parameter 'id' is not present"))
                                    .build()
                    )
    );

    public static final Response<?> REQUEST_BODY_NOT_READABLE_EXCEPTION = Response.fail(
            RpcStatus.INVALID_ARGUMENT
                    .withError(
                            RpcError.builder()
                                    .metadata("", "application", "/mock/post")
                                    .exception(new InvalidArgumentException("Required request body is missing"))
                                    .build()
                    )
    );

    public static final Response<?> INVALID_FORMAT_EXCEPTION = Response.fail(
            RpcStatus.INVALID_ARGUMENT
                    .withError(
                            RpcError.builder()
                                    .metadata("", "application", "/mock/post")
                                    .exception(new InvalidArgumentException("Value [INVALID_TYPE] is not in values [NORMAL, MOCK]"))
                                    .build()
                    )
    );

    public static final Response<?> INVALID_FORMAT_EXCEPTION_DATE = Response.fail(
            RpcStatus.INVALID_ARGUMENT
                    .withError(
                            RpcError.builder()
                                    .metadata("", "application", "/mock/post")
                                    .exception(new InvalidArgumentException("Value [2020-01-01] is invalid"))
                                    .build()
                    )
    );

    public static final Response<?> INVALID_PARAMETER_EXCEPTION = Response.fail(
            RpcStatus.INVALID_ARGUMENT
                    .withError(
                            RpcError.builder()
                                    .metadata("", "application", "/mock/post")
                                    .exception(new InvalidArgumentException("User must not be Mock type"))
                                    .build()
                    )
    );

    public static final Response<?> BINDING_EXCEPTION = Response.fail(
            RpcStatus.INVALID_ARGUMENT
                    .withError(
                            RpcError.builder()
                                    .metadata("", "application", "/mock/post")
                                    .exception(new InvalidArgumentException("Field [name] is invalid, length must be between 0 and 10"))
                                    .build()
                    )
    );

    public static final Response<?> PERMISSION_DENIED = Response.fail(
            RpcStatus.PERMISSION_DENIED
                    .withError(
                            RpcError.builder()
                                    .metadata("", "application", "/mock/get")
                                    .exception(new PermissionDeniedException("Permission denied on resource '0' of user"))
                                    .build()
                    )
    );

    public static final Response<?> METHOD_NOT_SUPPORT = Response.fail(
            RpcStatus.NOT_FOUND
                    .withError(
                            RpcError.builder()
                                    .metadata("", "application", "/mock/get")
                                    .exception(new NotFoundException("Request method 'POST' not supported"))
                                    .build()
                    )
    );

    public static final Response<?> NO_HANDLER_EXCEPTION = Response.fail(
            RpcStatus.NOT_FOUND
                    .withError(
                            RpcError.builder()
                                    .metadata("", "application", "/mock/not_exist_path")
                                    .exception(new NotFoundException("Resource [/mock/not_exist_path] you requested is not found"))
                                    .build()
                    )
    );

    public static final Response<?> RESOURCE_NOT_FOUND = Response.fail(
            RpcStatus.NOT_FOUND
                    .withError(
                            RpcError.builder()
                                    .metadata("", "application", "/mock/get")
                                    .exception(new NotFoundException("User -1 is not found"))
                                    .build()
                    )
    );

    public static void assertResponse(Response<?> expect, HttpStatus expectStatus,
                                      ResponseEntity<String> entity,
                                      ObjectMapper objectMapper) throws JsonProcessingException {
        Assertions.assertEquals(expectStatus, entity.getStatusCode());
        Response<?> actual = objectMapper.readValue(entity.getBody(), Response.class);

        Assertions.assertEquals(
                expect.getStatus().getCode(),
                actual.getStatus().getCode()
        );
        Assertions.assertEquals(
                expect.getStatus().getName(),
                actual.getStatus().getName()
        );
        Assertions.assertEquals(
                expect.getStatus().getDescription(),
                actual.getStatus().getDescription()
        );
        if (!expect.isOk()) {
            Assertions.assertEquals(
                    expect.getStatus().getError().getType(),
                    actual.getStatus().getError().getType()
            );
            Assertions.assertEquals(
                    expect.getStatus().getError().getMessage(),
                    actual.getStatus().getError().getMessage()
            );
            Assertions.assertEquals(
                    expect.getStatus().getError().getTraceId(),
                    actual.getStatus().getError().getTraceId()
            );
            Assertions.assertEquals(
                    expect.getStatus().getError().getService(),
                    actual.getStatus().getError().getService()
            );
            Assertions.assertEquals(
                    expect.getStatus().getError().getPath(),
                    actual.getStatus().getError().getPath()
            );
        }
    }
}
