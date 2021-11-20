package io.vzhidu.chassis.common.spring.stream;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.function.Function;

/**
 * 消息技术消费方法
 *
 * @param <T> 消息类型
 * @author Zhiqiang Du created at 2021/10/19
 */
@Slf4j
public class MessageCounterFunction<T> implements Function<Flux<T>, Flux<Integer>> {

    /**
     * 默认的窗口时间
     */
    public static final long DEFAULT_WINDOW_DURATION = 30;

    /**
     * 窗口取数时间
     */
    private final long windowDurationInSeconds;

    /**
     * 默认的构造函数，窗口时间为30秒
     */
    public MessageCounterFunction() {
        this.windowDurationInSeconds = DEFAULT_WINDOW_DURATION;
    }

    /**
     * 构造函数
     *
     * @param windowDurationInSeconds 窗口取数时间
     */
    public MessageCounterFunction(long windowDurationInSeconds) {
        this.windowDurationInSeconds = windowDurationInSeconds;
    }

    /**
     * Applies this function to the given argument.
     *
     * @param flux the function argument
     * @return the function result
     */
    @Override
    public Flux<Integer> apply(Flux<T> flux) {
        return flux.window(Duration.ofSeconds(windowDurationInSeconds))
                .flatMap(window -> window.reduce(new ArrayList<>(), (a, b) -> {
                    a.add(b);
                    return a;
                }))
                .map(ArrayList::size);
    }
}
