package io.vzhidu.chassis.common.core.ex;

import io.vzhidu.chassis.common.core.rpc.HttpStatus;
import io.vzhidu.chassis.common.core.rpc.RpcStatus;

/**
 * 客户端参数错误
 *
 * @author Zhiqiang Du
 */
public class InvalidArgumentException extends ClientException {

    private static final long serialVersionUID = 3457291020748185903L;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public InvalidArgumentException(String message) {
        super(message, RpcStatus.INVALID_ARGUMENT, HttpStatus.BAD_REQUEST);
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
    public InvalidArgumentException(String message, Throwable cause) {
        super(message, cause, RpcStatus.INVALID_ARGUMENT, HttpStatus.BAD_REQUEST);
    }
}
