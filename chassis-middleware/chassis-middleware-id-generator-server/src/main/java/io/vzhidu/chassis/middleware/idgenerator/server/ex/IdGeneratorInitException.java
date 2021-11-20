package io.vzhidu.chassis.middleware.idgenerator.server.ex;

/**
 * Exception throw out when initialize generator configuration.
 *
 * @author Zhiqiang Du Created at 2020/8/21
 */
public class IdGeneratorInitException extends RuntimeException {

    private static final long serialVersionUID = 8485888157652267184L;

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public IdGeneratorInitException(String message) {
        super(message);
    }
}
