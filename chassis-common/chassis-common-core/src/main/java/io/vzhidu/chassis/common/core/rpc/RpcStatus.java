package io.vzhidu.chassis.common.core.rpc;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

/**
 * The standard error model definition for RPC APIs.
 *
 * @author Zhiqiang Du Created at 2019/9/24
 */
@NoArgsConstructor
public final class RpcStatus {

    /**
     * The canonical error codes for RPC APIs.
     */
    public enum Code {

        /**
         * Not an error, returned on success
         * <p>
         * HTTP mapping: 200 OK
         */
        OK(0,
                null),

        /**
         * The client specified an invalid argument.
         * <p>
         * Note that this differs from {@link Code#FAILED_PRECONDITION}, `INVALID_ARGUMENT`
         * indicates arguments that are problematic regardless of the state of the system
         * <p>
         * HTTP mapping: 400 Bad Request
         */
        INVALID_ARGUMENT(1,
                "Client specified an invalid argument. Check error message for more information."),

        /**
         * The operation was rejected because the system is not in a state
         * required for the operation's execution.
         * <p>
         * For example, the directory to be deleted is non-empty, an `rmdir` operation
         * is applied to a non-directory.
         * <p>
         * Service developers can use the following guidelines to decide between
         * `FAILED_PRECONDITION`, {@link Code#ABORTED} and {@link Code#UNAVAILABLE}:
         * (a) Use {@link Code#UNAVAILABLE} if the client should retry just the failing call.
         * (b) Use {@link Code#ABORTED} if the client should retry at a higher level
         * (e.g., when a client-specified test-and-set fails, indicating the
         * client should restart a read-modify-write sequence).
         * (c) Use `FAILED_PRECONDITION` if the client should not retry until the system
         * state has been explicitly fixed. For example, if an `rmdir` fails because the directory
         * is non-empty, `FAILED_PRECONDITION` should be returned since the client should not retry
         * unless the files are deleted from the directory.
         * <p>
         * HTTP mapping: 400 Bad Request
         */
        FAILED_PRECONDITION(2,
                "Request can not be executed in the current system state."),

        /**
         * The operation was attempted past the valid range.  E.g., seeking or
         * reading past end-of-file.
         * <p>
         * Unlike `INVALID_ARGUMENT`, this error indicates a problem that may
         * be fixed if the system state changes. For example, a 32-bit file
         * system will generate `INVALID_ARGUMENT` if asked to read at an
         * offset that is not in the range [0,2^32-1], but it will generate
         * `OUT_OF_RANGE` if asked to read from an offset past the current
         * file size.
         * <p>
         * There is a fair bit of overlap between `FAILED_PRECONDITION` and
         * `OUT_OF_RANGE`.  We recommend using `OUT_OF_RANGE` (the more specific
         * error) when it applies so that callers who are iterating through
         * a space can easily look for an `OUT_OF_RANGE` error to detect when
         * they are done.
         * <p>
         * HTTP Mapping: 400 Bad Request
         */
        OUT_OF_RANGE(3,
                "Client specified an invalid range."),

        /**
         * The request does not have valid authentication credentials for the operation.
         * <p>
         * HTTP mapping: 401 Unauthorized
         */
        UNAUTHENTICATED(4,
                "Request not authenticated due to missing, invalid, or expired credentials."),

        /**
         * The caller does not have permission to execute the specified
         * operation.
         * <p>
         * `PERMISSION_DENIED` must not be used for rejections
         * cause by exhausting some resource (use {@link Code#RESOURCE_EXHAUSTED}
         * instead of those errors).
         * `PERMISSION_DENIED` must not be used if the caller can not be identified
         * (use {@link Code#UNAUTHENTICATED} instead of those errors).
         * <p>
         * This error does not imply the request is valid or the request entity exists
         * or satisfies other per-conditions.
         * <p>
         * HTTP mapping: 403 Forbidden
         */
        PERMISSION_DENIED(5,
                "Client does not have sufficient permission"),

        /**
         * Some requested entity (e.g., file or directory) was not found.
         * <p>
         * Note to server developers: if a request is denied for an entire class of users,
         * such as gradual feature roll out or undocumented whitelist, `NOT_FOUND` may be
         * used. If a request is denied for some users within a class of users, such as user-based
         * access control, {@link Code#PERMISSION_DENIED} must be used.
         * <p>
         * HTTP mapping: 404 Not Found
         */
        NOT_FOUND(6,
                "A specified resource is not found."),

        /**
         * The operation was aborted, typically due to a concurrency issue such as a sequencer check
         * failure or transaction abort.
         * <p>
         * See the guidelines above fro deciding between {@link Code#FAILED_PRECONDITION}, `ABORTED`
         * and {@link Code#UNAVAILABLE}
         * </p>
         * HTTP mapping: 409 Conflict
         */
        ABORTED(7,
                "Concurrency conflict, such as read-modify-write conflict."),

        /**
         * The entity that a client attempted to crate (e.g., file or directory) already exists.
         * <p>
         * HTTP mapping: 409 Conflict
         */
        ALREADY_EXISTS(8,
                "The resource that a client tried to create already exists."),

        /**
         * Some resource has been exhausted, perhaps a per-user quota, or
         * perhaps the entire file system is out of space.
         * <p>
         * HTTP mapping: 429 Too many request
         */
        RESOURCE_EXHAUSTED(9,
                "Either out of resource quota or reaching rate limiting, Check error message for more information."),

        /**
         * The operation was cancelled, typically by the caller.
         * <p>
         * HTTP mapping: 499 Client Closed Request
         */
        CANCELLED(10,
                "Request cancelled by the client."),

        /**
         * Unrecoverable data loss or corruption.
         * <p>
         * HTTP mapping:500 Internal Server RpcStatus
         */
        DATA_LOSS(11,
                "Unrecoverable data loss or data corruption. The client should report the error to the user."),

        /**
         * Unknown error.
         * <p>
         * For example, errors raised by APIs that do not return enough error information
         * may be converted to this error.
         * <p>
         * HTTP mapping: Internal Server RpcStatus
         */
        UNKNOWN(12,
                "Unknown server error, please contact system staff."),

        /**
         * Internal errors.
         * <p>
         * This means that some invariants expected by this underlying system have been
         * broken. This error code is reserved for serious errors.
         * <p>
         * HTTP mapping: 500 Internal Server RpcStatus
         */
        INTERNAL(13,
                "Internal server error, please contact system staff."),

        /**
         * The operation is not implemented or it not supported/enabled in this service.
         * <p>
         * HTTP mapping: 501 Not Implemented
         */
        UNIMPLEMENTED(14,
                "API method not implemented by the server."),

        /**
         * The service is currently unavailable.
         * <p>
         * This is most likely a transient condition, which can be corrected by retrying
         * with a backoff.
         * <p>
         * See the guidelines above for deciding between {@link Code#FAILED_PRECONDITION},
         * {@link Code#ABORTED} and `UNAVAILABLE`.
         * <p>
         * Http mapping: 503 Service Unavailable
         */
        UNAVAILABLE(15,
                "Service unavailable, please contact system staff."),

        /**
         * The deadline expired before the operation could complete. For operations
         * that change the state of the system, this error may be returned even if the
         * operation has completed successfully.
         * <p>
         * For example, a successfully response from a server could have been delayed
         * long enough for the deadline to expire.
         * <p>
         * HTTP mapping: 504 Gateway Timeout
         */
        DEADLINE_EXCEEDED(16, "Request deadline exceeded.");

        /**
         * The simple value of Code
         */
        private final int value;

        /**
         * The short description of Code
         */
        private final String desc;

        /**
         * Default constructor
         *
         * @param value integer value of business code
         */
        Code(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        /**
         * Returns int value of this enum
         *
         * @return int value of this enum
         */
        public int value() {
            return value;
        }

        /**
         * Returns short description of this enum
         *
         * @return short description of this enum
         */
        public String getDesc() {
            return desc;
        }

        /**
         * Convert enum to {@link RpcStatus}
         *
         * @return the {@link RpcStatus} mapping
         */
        public RpcStatus toStatus() {
            return RPC_STATUS_LIST.get(value);
        }
    }

    /**
     * Hold RPC status list
     */
    private static final List<RpcStatus> RPC_STATUS_LIST = buildStatusList();

    /**
     * Build RPC status list based on {@link Code}
     */
    private static List<RpcStatus> buildStatusList() {
        TreeMap<Integer, RpcStatus> errorTreeMap = new TreeMap<>();
        for (Code code : Code.values()) {
            RpcStatus replaced = errorTreeMap.put(code.value(), new RpcStatus(code));
            if (replaced != null) {
                throw new IllegalStateException("Code value duplication between " +
                        replaced.getName().name() + " & " + code.name());
            }
        }
        return new ArrayList<>(errorTreeMap.values());
    }

    //region RpcStatus list

    /**
     * The operation completed successfully.
     */
    public static final RpcStatus OK = Code.OK.toStatus();
    /**
     * The operation was cancelled (typically by the caller).
     */
    public static final RpcStatus CANCELLED = Code.CANCELLED.toStatus();
    /**
     * Unknown error. See {@link Code#UNKNOWN}.
     */
    public static final RpcStatus UNKNOWN = Code.UNKNOWN.toStatus();
    /**
     * Client specified an invalid argument. See {@link Code#INVALID_ARGUMENT}.
     */
    public static final RpcStatus INVALID_ARGUMENT = Code.INVALID_ARGUMENT.toStatus();
    /**
     * Deadline expired before operation could complete. See {@link Code#DEADLINE_EXCEEDED}.
     */
    public static final RpcStatus DEADLINE_EXCEEDED = Code.DEADLINE_EXCEEDED.toStatus();
    /**
     * Some requested entity (e.g., file or directory) was not found.
     */
    public static final RpcStatus NOT_FOUND = Code.NOT_FOUND.toStatus();
    /**
     * Some entity that we attempted to create (e.g., file or directory) already exists.
     */
    public static final RpcStatus ALREADY_EXISTS = Code.ALREADY_EXISTS.toStatus();
    /**
     * The caller does not have permission to execute the specified operation. See {@link Code#PERMISSION_DENIED}.
     */
    public static final RpcStatus PERMISSION_DENIED = Code.PERMISSION_DENIED.toStatus();
    /**
     * The request does not have valid authentication credentials for the operation.
     */
    public static final RpcStatus UNAUTHENTICATED = Code.UNAUTHENTICATED.toStatus();
    /**
     * Some resource has been exhausted, perhaps a per-user quota, or perhaps the entire file system
     * is out of space.
     */
    public static final RpcStatus RESOURCE_EXHAUSTED = Code.RESOURCE_EXHAUSTED.toStatus();
    /**
     * Operation was rejected because the system is not in a state required for the operation's
     * execution. See {@link Code#FAILED_PRECONDITION}.
     */
    public static final RpcStatus FAILED_PRECONDITION = Code.FAILED_PRECONDITION.toStatus();
    /**
     * The operation was aborted, typically due to a concurrency issue like sequencer check failures,
     * transaction aborts, etc. See {@link Code#ABORTED}.
     */
    public static final RpcStatus ABORTED = Code.ABORTED.toStatus();
    /**
     * Operation was attempted past the valid range. See {@link Code#OUT_OF_RANGE}.
     */
    public static final RpcStatus OUT_OF_RANGE = Code.OUT_OF_RANGE.toStatus();
    /**
     * Operation is not implemented or not supported/enabled in this service.
     */
    public static final RpcStatus UNIMPLEMENTED = Code.UNIMPLEMENTED.toStatus();
    /**
     * Internal errors. See {@link Code#INTERNAL}.
     */
    public static final RpcStatus INTERNAL = Code.INTERNAL.toStatus();
    /**
     * The service is currently unavailable. See {@link Code#UNAVAILABLE}.
     */
    public static final RpcStatus UNAVAILABLE = Code.UNAVAILABLE.toStatus();
    /**
     * Unrecoverable data loss or corruption.
     */
    public static final RpcStatus DATA_LOSS = Code.DATA_LOSS.toStatus();

    //endregion

    /**
     * Returns {@link RpcStatus} given a canonical error {@link Code} value.
     *
     * @param codeValue code value of {@link Code}.
     * @return see {@link RpcStatus}
     */
    public static RpcStatus fromStatusCodeValue(int codeValue) {
        if (codeValue < 0 || codeValue > RPC_STATUS_LIST.size()) {
            return UNKNOWN;
        } else {
            return RPC_STATUS_LIST.get(codeValue);
        }
    }

    /**
     * Returns {@link RpcStatus} given a canonical error {@link Code}.
     *
     * @param code code value of {@link Code}.
     * @return see {@link RpcStatus}
     */
    public static RpcStatus fromStatusCode(Code code) {
        return code.toStatus();
    }

    /**
     * A simple error code that can be easily handled by the client.
     * <p>
     * The actual error code is defined by {@link Code}
     */
    private int code;

    /**
     * A short description about code means
     */
    private Code name;

    /**
     * A developer-facing human-readable error message in English.
     * It should both explain the error and offer an actionable resolution to it.
     */
    private String description;

    /**
     * Additional error information that the client code can use to handle
     * the error, such as retry delay or a help link.
     */
    private RpcError error;

    /**
     * Default constructor
     */
    private RpcStatus(Code code) {
        this.code = code.value();
        this.name = code;
    }

    /**
     * Full arguments constructor
     *
     * @param code  the simple error code
     * @param name  the reason phrase
     * @param error extra error details
     */
    private RpcStatus(int code, Code name, RpcError error) {
        this.code = code;
        this.name = name;
        this.description = name.getDesc();
        this.error = error;
    }

    /**
     * Add error details int status.
     */
    public RpcStatus withError(RpcError error) {
        if (Objects.equals(this.error, error)) {
            return this;
        }

        return new RpcStatus(this.code, this.name, error);
    }

    //region Getter

    public int getCode() {
        return code;
    }

    public Code getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public RpcError getError() {
        return error;
    }

    @Override
    public String toString() {
        return "RpcStatus{" +
                "code=" + code +
                ", name=" + name +
                ", error=" + error +
                '}';
    }

    //endregion
}

