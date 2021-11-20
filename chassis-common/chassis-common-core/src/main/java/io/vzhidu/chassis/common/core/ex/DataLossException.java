package io.vzhidu.chassis.common.core.ex;

import io.vzhidu.chassis.common.core.rpc.HttpStatus;
import io.vzhidu.chassis.common.core.rpc.RpcStatus;

/**
 * 出现不可恢复的数据丢失或数据损坏。客户端应该向用户报告错误
 *
 * @author Zhiqiang Du created at 2021/10/12
 */
public class DataLossException extends ServerException {

    private static final long serialVersionUID = -4215751995200781283L;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public DataLossException(String message) {
        super(message, RpcStatus.DATA_LOSS, HttpStatus.INTERNAL_SERVER_ERROR);
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
    public DataLossException(String message, Throwable cause) {
        super(message, cause, RpcStatus.DATA_LOSS, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
