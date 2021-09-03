package io.vzhidu.chassis.common.core.rpc;

import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The standard and unified response model definition for RPC APIs.
 *
 * @param <T> the content type of resource
 * @author Zhiqiang Du Created at 2019/9/24
 */
@ToString(callSuper = true)
@NoArgsConstructor
public final class Response<T> extends AbstractRpcResponse {

    /**
     * Construct a error response based on {@link RpcStatus}
     */
    public static Response<?> fail(RpcStatus status) {
        return new Response<>(status);
    }

    /**
     * Construct a success response with data.
     */
    public static <T> Response<T> ok(T data) {
        return new Response<>(data, RpcStatus.OK);
    }

    /**
     * The resource based on a generic type returned by the operations.
     */
    private T data;

    /**
     * Default constructor
     */
    private Response(T data, RpcStatus status) {
        super(status);
        this.data = data;
    }

    /**
     * Default constructor
     */
    private Response(RpcStatus status) {
        super(status);
    }

    /**
     * Returns data that response hold.
     */
    public T getData() {
        return data;
    }
}
