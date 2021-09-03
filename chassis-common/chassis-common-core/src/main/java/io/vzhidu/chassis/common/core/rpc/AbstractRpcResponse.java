package io.vzhidu.chassis.common.core.rpc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

/**
 * Abstract RPC response schema definition.
 *
 * @author Zhiqiang Du Created at 2020/9/24
 */
@ToString
@NoArgsConstructor
public abstract class AbstractRpcResponse {

    /**
     * The error information if some errors occur through the operations.
     * <p>
     * See {@link RpcStatus}
     */
    private RpcStatus status;

    /**
     * Construct a response with given {@link RpcStatus}
     */
    protected AbstractRpcResponse(RpcStatus status) {
        this.status = status;
    }

    /**
     * Returns rpc status in response, see {@link RpcStatus}.
     */
    public RpcStatus getStatus() {
        return status;
    }

    /**
     * Return true or false when response has error or not.
     */
    @JsonIgnore
    public boolean isOk() {
        return Objects.equals(this.status.getName(), RpcStatus.Code.OK);
    }
}
