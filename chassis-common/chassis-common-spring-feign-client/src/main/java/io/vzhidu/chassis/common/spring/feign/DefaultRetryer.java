package io.vzhidu.chassis.common.spring.feign;

import io.vzhidu.chassis.common.core.ex.ClientException;
import io.vzhidu.chassis.common.core.ex.ServerException;
import io.vzhidu.chassis.common.core.ex.UnAvailableException;
import feign.RetryableException;
import feign.Retryer;
import lombok.SneakyThrows;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * 默认的重试策略
 *
 * @author Zhiqiang Du created at 2021/10/12
 */
public class DefaultRetryer implements Retryer {

    private static final long DEFAULT_PERIOD = 100L;
    private static final long DEFAULT_MAX_PERIOD = 1L;
    private static final int DEFAULT_MAX_ATTEMPTS = 5;
    private static final double DEFAULT_BACKOFF_INDEX = 1.5;

    private final int maxAttempts;
    private final long period;
    private final long maxPeriod;
    private int attempt;
    private long sleptForMillis;

    /**
     * 返回默认的重试器
     */
    public DefaultRetryer() {
        this(DEFAULT_PERIOD, SECONDS.toMillis(DEFAULT_MAX_PERIOD), DEFAULT_MAX_ATTEMPTS);
    }

    /**
     * 根据参数返回对应的重试器
     *
     * @param period      重试间隔
     * @param maxPeriod   最大重试间隔
     * @param maxAttempts 最大重试次数
     */
    public DefaultRetryer(long period, long maxPeriod, int maxAttempts) {
        this.period = period;
        this.maxPeriod = maxPeriod;
        this.maxAttempts = maxAttempts;
        this.attempt = 1;
    }

    /**
     * visible for testing
     */
    protected long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * if retry is permitted, return (possibly after sleeping). Otherwise, propagate the exception.
     */
    @SneakyThrows
    @Override
    public void continueOrPropagate(RetryableException e) {
        if (attempt++ >= maxAttempts) {
            thrownAfterRetry(e);
        }

        long interval;
        if (e.retryAfter() != null) {
            interval = e.retryAfter().getTime() - currentTimeMillis();
            if (interval > maxPeriod) {
                interval = maxPeriod;
            }
            if (interval < 0) {
                return;
            }
        } else {
            interval = nextMaxInterval();
        }
        try {
            Thread.sleep(interval);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
            throw e;
        }
        sleptForMillis += interval;
    }

    /**
     * Calculates the time interval to a retry attempt. <br>
     * The interval increases exponentially with each attempt, at a rate of nextInterval *= 1.5
     * (where 1.5 is the backoff factor), to the maximum interval.
     *
     * @return time in nanoseconds from now until the next attempt.
     */
    long nextMaxInterval() {
        long interval = (long) (period * Math.pow(DEFAULT_BACKOFF_INDEX, attempt - 1));
        return Math.min(interval, maxPeriod);
    }

    /**
     * Copy a retryer base on the existed one
     */
    @Override
    public Retryer clone() {
        return new DefaultRetryer(period, maxPeriod, maxAttempts);
    }

    @SneakyThrows
    private void thrownAfterRetry(RetryableException e) {
        Throwable cause = e.getCause();
        if (cause instanceof ClientException || cause instanceof ServerException) {
            throw cause;
        } else {
            throw new UnAvailableException(e.getMessage(), e.getCause());
        }
    }
}
