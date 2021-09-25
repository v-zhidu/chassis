package io.vzhidu.chassis.middleware.client.idgenerator;

import io.vzhidu.chassis.common.core.rpc.Response;
import io.vzhidu.chassis.common.core.rpc.RpcStatus;
import io.vzhidu.chassis.middleware.core.idgenerator.IdGenerator;
import io.vzhidu.chassis.middleware.core.idgenerator.ex.IdGenerateException;
import io.vzhidu.chassis.middleware.core.idgenerator.ex.NoSuchKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

/**
 * Feign implementation of {@link IdGenerator}
 *
 * @author Zhiqiang Du Created at 2020/7/1
 */
@Slf4j
@Service
class IdGeneratorFeignImpl implements IdGenerator {

    private final IdGeneratorServiceFeignClient client;

    IdGeneratorFeignImpl(IdGeneratorServiceFeignClient client) {
        this.client = client;
    }

    @Override
    public long getSegmentId(@Nonnull String key) throws NoSuchKeyException {
        String data = retrieveData(client.getSegmentId(key));
        return Long.parseLong(data);
    }

    @Override
    public long getSnowflakeId(@Nonnull String key) {
        String data = retrieveData(client.getSnowflakeId(key));
        return Long.parseLong(data);
    }

    private String retrieveData(Response<String> response) {
        if (response == null) {
            throw new IdGenerateException("Received empty data from identifier generator server");
        }

        RuntimeException ex;
        if (!response.isOk()) {
            //此处的错误处理逻辑抽象为统一逻辑，使用{@link feign.ErrorDecoder}实现
            String errMsg = response.getStatus().getDescription();
            switch (RpcStatus.fromStatusCodeValue(response.getStatus().getCode()).getName()) {
                case NOT_FOUND:
                    ex = new NoSuchKeyException(errMsg);
                    break;
                case INVALID_ARGUMENT:
                    ex = new IllegalArgumentException(errMsg);
                    break;
                default:
                    ex = new IdGenerateException(errMsg);
            }
            throw ex;
        }

        return response.getData();
    }
}
