package io.vzhidu.chassis.middleware.idgenerator.server.service.converter;

import com.sankuai.inf.leaf.segment.model.LeafAlloc;
import io.vzhidu.chassis.middleware.idgenerator.core.BizTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Mapper between {@link BizTag} and {@link LeafAlloc}
 */
@Mapper
public interface BizTagMapper {

    BizTagMapper INSTANCE = Mappers.getMapper(BizTagMapper.class);

    @Mapping(target = "key", source = "key")
    @Mapping(target = "maxId", source = "maxId")
    @Mapping(target = "step", source = "step")
    @Mapping(target = "updateTime", source = "updateTime")
    BizTag leafAllocToBizTag(LeafAlloc alloc);

    List<BizTag> leafAllocsToBizTags(List<LeafAlloc> allocs);
}
