package io.vzhidu.chassis.common.core.util;

import javax.annotation.Nullable;

/**
 * A collection of static utility methods to validate input.
 * <p>
 * This class is modelled after Google Guava's Preconditions class, and partly takes code
 * from that class.
 * <p>
 * Copied from Flink. We add this code to the code base in order to reduce external dependencies.
 *
 * @author Zhiqiang Du
 */
public final class Preconditions {

    // ------------------------------------------------------------------------
    //  Null checks
    // ------------------------------------------------------------------------

    /**
     * Ensures that the given object reference is not null.
     * Upon violation, a {@code NullPointerException} with no message is thrown.
     *
     * @param reference The object reference
     * @return The object reference itself (generically typed).
     * @throws NullPointerException Thrown, if the passed reference was null.
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    /**
     * Ensures that the given object reference is not null.
     * Upon violation, a {@code NullPointerException} with the given message is thrown.
     *
     * @param reference    The object reference
     * @param errorMessage The message for the {@code NullPointerException} that is thrown if the check fails.
     * @return The object reference itself (generically typed).
     * @throws NullPointerException Thrown, if the passed reference was null.
     */
    public static <T> T checkNotNull(T reference, @Nullable String errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    /**
     * Ensures that the given object reference is not null.
     * Upon violation, a {@code NullPointerException} with the given message is thrown.
     *
     * <p>The error message is constructed from a template and an arguments array, after
     * a similar fashion as {@link String#format(String, Object...)}, but supporting only
     * {@code %s} as a placeholder.
     *
     * @param reference            The object reference
     * @param errorMessageTemplate The message template for the {@code NullPointerException}
     *                             that is thrown if the check fails. The template substitutes its
     *                             {@code %s} placeholders with the error message arguments.
     * @param errorMessageArgs     The arguments for the error message, to be inserted into the
     *                             message template for the {@code %s} placeholders.
     * @return The object reference itself (generically typed).
     * @throws NullPointerException Thrown, if the passed reference was null.
     */
    public static <T> T checkNotNull(T reference,
                                     @Nullable String errorMessageTemplate,
                                     @Nullable Object... errorMessageArgs) {

        if (reference == null) {
            throw new NullPointerException(format(errorMessageTemplate, errorMessageArgs));
        }
        return reference;
    }

    // ------------------------------------------------------------------------
    //  Boolean Condition Checking (Argument)
    // ------------------------------------------------------------------------

    /**
     * Checks the given boolean condition, and throws an {@code IllegalArgumentException} if
     * the condition is not met (evaluates to {@code false}).
     *
     * @param condition The condition to check
     * @throws IllegalArgumentException Thrown, if the condition is violated.
     */
    public static void checkArgument(boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Checks the given boolean condition, and throws an {@code IllegalArgumentException} if
     * the condition is not met (evaluates to {@code false}). The exception will have the
     * given error message.
     *
     * @param condition    The condition to check
     * @param errorMessage The message for the {@code IllegalArgumentException} that is thrown if the check fails.
     * @throws IllegalArgumentException Thrown, if the condition is violated.
     */
    public static void checkArgument(boolean condition, @Nullable Object errorMessage) {
        if (!condition) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    /**
     * Checks the given boolean condition, and throws an {@code IllegalArgumentException} if
     * the condition is not met (evaluates to {@code false}).
     *
     * @param condition            The condition to check
     * @param errorMessageTemplate The message template for the {@code IllegalArgumentException}
     *                             that is thrown if the check fails. The template substitutes its
     *                             {@code %s} placeholders with the error message arguments.
     * @param errorMessageArgs     The arguments for the error message, to be inserted into the
     *                             message template for the {@code %s} placeholders.
     * @throws IllegalArgumentException Thrown, if the condition is violated.
     */
    public static void checkArgument(boolean condition,
                                     @Nullable String errorMessageTemplate,
                                     @Nullable Object... errorMessageArgs) {

        if (!condition) {
            throw new IllegalArgumentException(format(errorMessageTemplate, errorMessageArgs));
        }
    }

    // ------------------------------------------------------------------------
    //  Boolean Condition Checking (State)
    // ------------------------------------------------------------------------

    /**
     * Checks the given boolean condition, and throws an {@code IllegalStateException} if
     * the condition is not met (evaluates to {@code false}).
     *
     * @param condition The condition to check
     * @throws IllegalStateException Thrown, if the condition is violated.
     */
    public static void checkState(boolean condition) {
        if (!condition) {
            throw new IllegalStateException();
        }
    }

    /**
     * Checks the given boolean condition, and throws an {@code IllegalStateException} if
     * the condition is not met (evaluates to {@code false}). The exception will have the
     * given error message.
     *
     * @param condition    The condition to check
     * @param errorMessage The message for the {@code IllegalStateException} that is thrown if the check fails.
     * @throws IllegalStateException Thrown, if the condition is violated.
     */
    public static void checkState(boolean condition, @Nullable Object errorMessage) {
        if (!condition) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }

    /**
     * Checks the given boolean condition, and throws an {@code IllegalStateException} if
     * the condition is not met (evaluates to {@code false}).
     *
     * @param condition            The condition to check
     * @param errorMessageTemplate The message template for the {@code IllegalStateException}
     *                             that is thrown if the check fails. The template substitutes its
     *                             {@code %s} placeholders with the error message arguments.
     * @param errorMessageArgs     The arguments for the error message, to be inserted into the
     *                             message template for the {@code %s} placeholders.
     * @throws IllegalStateException Thrown, if the condition is violated.
     */
    public static void checkState(boolean condition,
                                  @Nullable String errorMessageTemplate,
                                  @Nullable Object... errorMessageArgs) {

        if (!condition) {
            throw new IllegalStateException(format(errorMessageTemplate, errorMessageArgs));
        }
    }

    /**
     * Ensures that the given index is valid for an array, list or string of the given size.
     *
     * @param index index to check
     * @param size  size of the array, list or string
     * @throws IllegalArgumentException  Thrown, if size is negative.
     * @throws IndexOutOfBoundsException Thrown, if the index negative or greater than or equal to size
     */
    public static void checkElementIndex(int index, int size) {
        checkArgument(size >= 0, "Size was negative.");
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /**
     * Ensures that the given index is valid for an array, list or string of the given size.
     *
     * @param index        index to check
     * @param size         size of the array, list or string
     * @param errorMessage The message for the {@code IndexOutOfBoundsException} that is thrown if the check fails.
     * @throws IllegalArgumentException  Thrown, if size is negative.
     * @throws IndexOutOfBoundsException Thrown, if the index negative or greater than or equal to size
     */
    public static void checkElementIndex(int index, int size, @Nullable String errorMessage) {
        checkArgument(size >= 0, "Size was negative.");
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(errorMessage + " Index: " + index + ", Size: " + size);
        }
    }

    // ------------------------------------------------------------------------
    //  Utilities
    // ------------------------------------------------------------------------

    /**
     * 参数限制的字符大小
     */
    private static final int ARGS_MAX_SIZE = 16;

    /**
     * 占位符
     */
    private static final String PLACE_HOLDER = "%s";

    /**
     * A simplified formatting method. Similar to {@link String#format(String, Object...)}, but
     * with lower overhead (only String parameters, no locale, no format validation).
     *
     * <p>This method is taken quasi verbatim from the Guava Preconditions class.
     */
    private static String format(@Nullable String template, @Nullable Object... args) {
        final int numArgs = args == null ? 0 : args.length;
        // null -> "null"
        template = String.valueOf(template);

        // start substituting the arguments into the '%s' placeholders
        StringBuilder builder = new StringBuilder(template.length() + ARGS_MAX_SIZE * numArgs);
        int templateStart = 0;
        int i = 0;
        while (i < numArgs) {
            int placeholderStart = template.indexOf(PLACE_HOLDER, templateStart);
            if (placeholderStart < 0) {
                break;
            }
            builder.append(template, templateStart, placeholderStart);
            builder.append(args[i++]);
            templateStart = placeholderStart + PLACE_HOLDER.length();
        }
        builder.append(template.substring(templateStart));

        // if we run out of placeholders, append the extra args in square braces
        if (i < numArgs) {
            builder.append(" [");
            builder.append(args[i++]);
            while (i < numArgs) {
                builder.append(", ");
                builder.append(args[i++]);
            }
            builder.append(']');
        }

        return builder.toString();
    }

    // ------------------------------------------------------------------------

    /**
     * Private constructor to prevent instantiation.
     */
    private Preconditions() {
    }
}

