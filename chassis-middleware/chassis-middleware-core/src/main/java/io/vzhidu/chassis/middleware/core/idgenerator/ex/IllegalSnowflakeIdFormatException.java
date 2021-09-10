package io.vzhidu.chassis.middleware.core.idgenerator.ex;

/**
 * 标识符格式化异常
 *
 * @author Zhiqiang Du Created at 2020/9/3
 */
public class IllegalSnowflakeIdFormatException extends IllegalArgumentException {

    private static final long serialVersionUID = -4647192350825311327L;

    private final Long illegalStr;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to .
     */
    public IllegalSnowflakeIdFormatException(Long illegalStr) {
        if (illegalStr == null) {
            throw new NullPointerException();
        }
        this.illegalStr = illegalStr;
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return "Illegal snowflake id format, id: " + this.illegalStr;
    }
}
