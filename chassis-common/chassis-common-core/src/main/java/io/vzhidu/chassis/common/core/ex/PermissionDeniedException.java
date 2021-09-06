package io.vzhidu.chassis.common.core.ex;

/**
 * 权限相关错误
 *
 * @author Zhiqiang Du
 */
public class PermissionDeniedException extends RuntimeException {

    private static final long serialVersionUID = 278733326049512895L;

    public PermissionDeniedException(String message) {
        super(message);
    }

    public PermissionDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * for better performance
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
