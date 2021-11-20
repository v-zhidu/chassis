package io.vzhidu.chassis.common.spring.stream;

/**
 * Interface of send data to global stream architecture
 *
 * @author Zhiqiang Du created at 2021/10/19
 */
public interface MessageStreamBridge {

    /**
     * Write the shopee message to the stream
     * destination is retrieve from {@link MessageStreamBridge#getOutputDestination()}
     *
     * @param message     message body wrapper by message
     */
    <T> void write(T message);

    /**
     * Write the shopee message to the stream
     *
     * @param destination target topic name
     * @param message     message body wrapper by message
     */
    <T> void write(String destination, T message);

    /**
     * 返回target topic name
     *
     * @return target topic name
     */
    default String getOutputDestination() {
        return null;
    }
}
