package io.vzhidu.chassis.common.core.ex;

import io.vzhidu.chassis.common.core.rpc.HttpStatus;
import io.vzhidu.chassis.common.core.rpc.RpcStatus;

/**
 * 服务端错误
 *
 * @author Zhiqiang Du created at 2021/10/12
 */
public abstract class ServerException extends RuntimeException {

    private static final long serialVersionUID = -1012195011733652196L;

    private final RpcStatus rpcStatus;

    private final HttpStatus httpStatus;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ServerException(String message, RpcStatus rpcStatus, HttpStatus httpStatus) {
        super(message);
        this.rpcStatus = rpcStatus;
        this.httpStatus = httpStatus;
    }

    /**
     * Constructs a new runtime exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A {@code null} value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @since 1.4
     */
    public ServerException(String message, Throwable cause, RpcStatus rpcStatus, HttpStatus httpStatus) {
        super(message, cause);
        this.rpcStatus = rpcStatus;
        this.httpStatus = httpStatus;
    }

    /**
     * for better performance
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    /**
     * Return the {@link RpcStatus}
     */
    public RpcStatus getRpcStatus() {
        return rpcStatus;
    }

    /**
     * Return the {@link HttpStatus}
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
