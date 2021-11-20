package io.vzhidu.chassis.middleware.idgenerator.client;

import io.vzhidu.chassis.common.core.ex.UnknownException;
import io.vzhidu.chassis.common.core.rpc.Response;
import io.vzhidu.chassis.middleware.idgenerator.core.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Feign implementation of {@link IdGenerator}
 *
 * @author Zhiqiang Du Created at 2020/7/1
 */
@Slf4j
@Service
class IdGeneratorFeignImpl implements IdGenerator {

    private final IdGeneratorFeignClient client;

    IdGeneratorFeignImpl(IdGeneratorFeignClient client) {
        this.client = client;
    }

    @Override
    public long getSegmentId(String key) {
        String data = retrieveData(client.getSegmentId(key));
        return Long.parseLong(data);
    }

    @Override
    public long getSnowflakeId(String key) {
        String data = retrieveData(client.getSnowflakeId(key));
        return Long.parseLong(data);
    }

    private String retrieveData(Response<String> response) {
        if (response == null) {
            throw new UnknownException("Received empty data from identifier generator server");
        }

        return response.getData();
    }
}
