package io.vzhidu.chassis.common.core.ex;

/**
 * 参数错误
 *
 * @author Zhiqiang Du
 */
public class InvalidParameterException extends RuntimeException {

    private static final long serialVersionUID = 3457291020748185903L;

    public InvalidParameterException(String message) {
        super(message);
    }

    public InvalidParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * for better performance
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
