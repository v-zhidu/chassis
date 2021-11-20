package io.vzhidu.chassis.common.core.rpc;

import io.vzhidu.chassis.common.core.ex.InternalException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.InvocationTargetException;

/**
 * RPC错误返回数据模型
 *
 * @author Zhiqiang Du
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class RpcError {

    /**
     * Returns an instance of {@link RpcError.Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 用于记录分布式追踪id
     */
    private String traceId;

    /**
     * 发生错误的服务名称
     */
    private String service;

    /**
     * 发生错误的资源路径
     */
    private String path;

    /**
     * 发生错误的资源类型
     */
    private String timestamp;

    /**
     * 发生的错误类型
     */
    private String type;

    /**
     * 错误信息
     */
    private String message;

    /**
     * The builder of {@link RpcError}
     */
    public static final class Builder {

        private String traceId;

        private String service;

        private String path;

        private String timestamp;

        private String type;

        private String message;

        /**
         * 设置错误发生时间
         *
         * @param timestamp 时间字符串
         * @return {@link RpcError.Builder}
         */
        public Builder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * 设置错误的元信息
         *
         * @param traceId 分布式id
         * @param service 服务名称
         * @param path    资源路径
         * @return {@link RpcError.Builder}
         */
        public Builder metadata(String traceId, String service, String path) {
            this.traceId = traceId;
            this.service = service;
            this.path = path;
            return this;
        }

        /**
         * 设置错误内容
         *
         * @param ex 异常
         * @return {@link RpcError.Builder}
         */
        public Builder exception(Exception ex) {
            return exception(ex.getMessage(), ex.getClass().getCanonicalName());
        }

        /**
         * 设置错误内容
         *
         * @param message 错误信息
         * @param type    错误类型
         * @return {@link RpcError.Builder}
         */
        public Builder exception(String message, String type) {
            this.message = message;
            this.type = type;
            return this;
        }

        /**
         * 构造{@link RpcError}
         */
        public RpcError build() {
            RpcError error = new RpcError();
            error.setTraceId(traceId);
            error.setService(service);
            error.setPath(path);
            error.setTimestamp(timestamp);
            error.setType(type);
            error.setMessage(message);

            return error;
        }
    }

    /**
     * Translate {@link RpcError} to {@link Exception} according the {@link RpcError#type}
     */
    public static Exception translateTo(RpcError error, Throwable cause) {
        if (error == null || error.getType() == null) {
            throw new IllegalStateException("RpcError is empty or invalid");
        }

        try {
            return (Exception) Class.forName(error.getType())
                    .getConstructor(String.class, Throwable.class).newInstance(error.getMessage(), cause);
        } catch (ClassNotFoundException |
                NoSuchMethodException |
                InstantiationException |
                IllegalAccessException |
                InvocationTargetException e) {
            return new InternalException(e.getMessage(), e);
        }
    }
}
