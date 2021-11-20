package io.vzhidu.chassis.middleware.idgenerator.server.service.converter;

import com.sankuai.inf.leaf.segment.model.LeafAlloc;
import com.sankuai.inf.leaf.segment.model.SegmentBuffer;
import io.vzhidu.chassis.middleware.idgenerator.core.BizTag;
import io.vzhidu.chassis.middleware.idgenerator.core.BizTagDetail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper between {@link BizTag} and {@link LeafAlloc}
 *
 * @author Zhiqiang Du Created at 2020/3/18
 */
@Mapper
public interface BizTagDetailMapper {

    BizTagDetailMapper INSTANCE = Mappers.getMapper(BizTagDetailMapper.class);

    /**
     * Mapping {@link SegmentBuffer} to {@link BizTagDetail}
     */
    default BizTagDetail segmentBufferToBizTagDetail(SegmentBuffer buffer) {
        if (buffer == null) {
            throw new NullPointerException("SegmentBuffer is empty");
        }
        BizTagDetail tagDetail = new BizTagDetail();
        tagDetail.setKey(buffer.getKey());
        tagDetail.setInitOk(buffer.isInitOk());
        tagDetail.setNextReady(buffer.isNextReady());
        tagDetail.setPosition(buffer.getCurrentPos());
        tagDetail.setValue0(buffer.getSegments()[0].getValue().get());
        tagDetail.setMax0(buffer.getSegments()[0].getMax());
        tagDetail.setStep0(buffer.getSegments()[0].getStep());
        tagDetail.setValue1(buffer.getSegments()[1].getValue().get());
        tagDetail.setMax1(buffer.getSegments()[1].getMax());
        tagDetail.setStep1(buffer.getSegments()[1].getStep());

        return tagDetail;
    }
}
