package io.vzhidu.chassis.middleware.server.idgenerator.service;

import com.sankuai.inf.leaf.IDGen;
import com.sankuai.inf.leaf.common.Result;
import com.sankuai.inf.leaf.common.Status;
import io.vzhidu.chassis.middleware.core.idgenerator.IdGenerator;
import io.vzhidu.chassis.middleware.core.idgenerator.ex.IdGenerateException;
import io.vzhidu.chassis.middleware.core.idgenerator.ex.NoSuchKeyException;
import io.vzhidu.chassis.middleware.server.idgenerator.ex.IdGeneratorInitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

/**
 * Implementation of {@link IdGeneratorServiceImpl}
 *
 * @author Zhiqiang Du Created at 2020/3/19
 */
@Slf4j
@Service
class IdGeneratorServiceImpl implements IdGenerator {

    private static final long NO_SUCH_KEY_RESPONSE_CODE = -2L;

    private final IDGen segmentIdGenerator;

    private final IDGen snowflakeIdGenerator;

    /**
     * Constructor with segment and snowflake generator implement by https://github.com/Meituan-Dianping/Leaf
     *
     * @param segmentIdGenerator   Segment Id Generator which implemented {@link IDGen}
     * @param snowflakeIdGenerator Snowflake Id Generator which implemented {@link IDGen}
     * @throws IdGeneratorInitException throw when init generator failed
     */
    IdGeneratorServiceImpl(@Qualifier("SegmentIdGenerator") IDGen segmentIdGenerator,
                           @Qualifier("SnowflakeIdGenerator") IDGen snowflakeIdGenerator) {
        this.segmentIdGenerator = segmentIdGenerator;
        this.snowflakeIdGenerator = snowflakeIdGenerator;
        init();
    }

    private void init() {
        // Init Id Generator
        if (segmentIdGenerator.init()) {
            log.info("Segment generator init successfully");
        } else {
            throw new IdGeneratorInitException("Segment generator init fail");
        }

        if (snowflakeIdGenerator.init()) {
            log.info("Snowflake generator init successfully");
        } else {
            throw new IdGeneratorInitException("Snowflake generator init fail");
        }
    }

    @Override
    public long getSegmentId(@Nonnull String key) {
        return get(key, segmentIdGenerator);
    }

    @Override
    public long getSnowflakeId(@Nonnull String key) {
        return get(key, snowflakeIdGenerator);
    }

    private static long get(@Nonnull String key, @Nonnull IDGen generator) {
        Result result = generator.get(key);
        log.info("Generate id, key: " + key + ", result: " + result.toString());
        if (result.getStatus().equals(Status.EXCEPTION)) {
            if (result.getId() == NO_SUCH_KEY_RESPONSE_CODE) {
                throw new NoSuchKeyException(key);
            } else {
                throw new IdGenerateException(result.toString());
            }
        }

        return result.getId();
    }
}
