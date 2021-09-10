package io.vzhidu.chassis.middleware.core.idgenerator.ex;

/**
 * 生成id未知错误
 *
 * @author Zhiqiang Du Created at 2020/9/3
 */
public class IdGenerateException extends RuntimeException {

    private static final long serialVersionUID = -5259920804570988056L;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public IdGenerateException(String message) {
        super("Id generate failed, msg: " + message);
    }
}
