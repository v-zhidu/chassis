package io.vzhidu.chassis.common.spring.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.GenericMessage;

/**
 * Send data to an output binding effectively bridging non-stream application with spring-cloud-stream.
 *
 * @author Zhiqiang Du created at 2021/10/19
 */
@Slf4j
public class GenericMessageStreamBridge implements MessageStreamBridge {

    private final StreamBridge streamBridge;

    public GenericMessageStreamBridge(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    /**
     * Write the shopee message to the stream
     * destination is retrieve from {@link MessageStreamBridge#getOutputDestination()}
     *
     * @param message message body wrapper by message
     */
    @Override
    public <T> void write(T message) {
        write(getOutputDestination(), message);
    }

    /**
     * Write the shopee message to the stream
     *
     * @param destination target topic name
     * @param message     message body wrapper by message
     */
    @Override
    public <T> void write(String destination, T message) {
        String finalDestination = destination != null && !destination.isBlank() ? destination : getOutputDestination();
        if (finalDestination == null) {
            throw new IllegalArgumentException("Stream delivery destination is null or empty.");
        }
        if (message == null) {
            throw new IllegalArgumentException("Stream message is null or empty, refuse to deliver.");
        }

        log.debug("Write shopee message to stream, destination: {}, message: {}", finalDestination, message);
        streamBridge.send(finalDestination, new GenericMessage<>(message));
    }
}
