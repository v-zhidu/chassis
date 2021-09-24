package io.vzhidu.chassis.middleware.server.idgenerator.service;

import com.sankuai.inf.leaf.IDGen;
import com.sankuai.inf.leaf.common.ZeroIDGen;
import com.sankuai.inf.leaf.segment.SegmentIDGenImpl;
import com.sankuai.inf.leaf.segment.model.LeafAlloc;
import com.sankuai.inf.leaf.segment.model.SegmentBuffer;
import io.vzhidu.chassis.middleware.core.idgenerator.BizTag;
import io.vzhidu.chassis.middleware.core.idgenerator.BizTagDetail;
import io.vzhidu.chassis.middleware.core.idgenerator.IdMonitor;
import io.vzhidu.chassis.middleware.core.idgenerator.SnowflakeContent;
import io.vzhidu.chassis.middleware.core.idgenerator.ex.IllegalSnowflakeIdFormatException;
import io.vzhidu.chassis.middleware.core.idgenerator.ex.NoSuchKeyException;
import io.vzhidu.chassis.middleware.server.idgenerator.service.mapper.BizTagDetailMapper;
import io.vzhidu.chassis.middleware.server.idgenerator.service.mapper.BizTagMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link IdMonitor}
 */
@Slf4j
@Service
class IdMonitorServiceImpl implements IdMonitor {

    private final IDGen segmentIdGenerator;

    /**
     * Constructor with segment and snowflake generator implement by https://github.com/Meituan-Dianping/Leaf
     *
     * @param segmentIdGenerator Segment Id Generator which implemented {@link IDGen}
     */
    IdMonitorServiceImpl(@Qualifier("SegmentIdGenerator") IDGen segmentIdGenerator) {
        this.segmentIdGenerator = segmentIdGenerator;
    }

    @Override
    public List<BizTag> getAllBizTags() {
        if (segmentIdGenerator instanceof ZeroIDGen) {
            return new ArrayList<>();
        }

        List<LeafAlloc> list = ((SegmentIDGenImpl) segmentIdGenerator).getAllLeafAllocs();
        return BizTagMapper.INSTANCE.leafAllocsToBizTags(list);
    }

    @Override
    public BizTagDetail getBizTag(@Nonnull String key) {
        if (segmentIdGenerator instanceof ZeroIDGen) {
            throw new IllegalArgumentException("You should config id-generator.segment.enable=true first");
        }

        Map<String, SegmentBuffer> cache = ((SegmentIDGenImpl) segmentIdGenerator).getCache();
        SegmentBuffer buffer = cache.get(key);
        if (buffer == null) {
            throw new NoSuchKeyException(key);
        }

        return BizTagDetailMapper.INSTANCE.segmentBufferToBizTagDetail(buffer);
    }

    @Override
    public SnowflakeContent decodeSnowflakeId(long snowflakeId) {
        return fromSnowflakeId(snowflakeId);
    }

    /**
     * Construct a snowflake content from id in long type.
     *
     * @param snowflakeId long type id
     * @return {@link SnowflakeContent}
     */
    private static SnowflakeContent fromSnowflakeId(long snowflakeId) {
        SnowflakeContent content = new SnowflakeContent();
        try {
            long originTimestamp = (snowflakeId >> 22) + 1288834974657L;
            Date date = new Date(originTimestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

            String timestamp = sdf.format(date);
            long workerId = (snowflakeId >> 12) ^ (snowflakeId >> 22 << 10);
            long sequence = snowflakeId ^ (snowflakeId >> 12 << 12);

            content.setId(String.valueOf(snowflakeId));
            content.setTimestamp(timestamp);
            content.setWorkerId(workerId);
            content.setSequenceId(sequence);
        } catch (DateTimeException ignored) {
            throw new IllegalSnowflakeIdFormatException(snowflakeId);
        }

        return content;
    }
}
