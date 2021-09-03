package io.vzhidu.chassis.common.core.ex;

/**
 * 资源相关错误
 *
 * @author Zhiqiang Du
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 2203128573172785541L;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
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
