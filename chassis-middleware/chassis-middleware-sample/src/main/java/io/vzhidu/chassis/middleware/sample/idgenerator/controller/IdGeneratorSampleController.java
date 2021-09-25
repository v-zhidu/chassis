package io.vzhidu.chassis.middleware.sample.idgenerator.controller;

import io.vzhidu.chassis.middleware.core.idgenerator.IdGenerator;
import io.vzhidu.chassis.middleware.core.idgenerator.ex.IdGenerateException;
import io.vzhidu.chassis.middleware.core.idgenerator.ex.NoSuchKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Id 生成服务使用示例
 *
 * @author Zhiqiang Du Created at 2020/9/1
 */
@RestController
public class IdGeneratorSampleController {

    private final IdGenerator idGenerator;

    public IdGeneratorSampleController(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    /**
     * get id
     */
    @GetMapping("/segments/{key}")
    public ResponseEntity<String> getSegmentId(@PathVariable("key") String key) {
        try {
            return ResponseEntity.ok(String.valueOf(idGenerator.getSegmentId(key)));
        } catch (NoSuchKeyException ignored) {
            return ResponseEntity.badRequest().build();
        } catch (IdGenerateException ignored) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    /**
     * get id
     */
    @GetMapping("/snowflakes/{key}")
    public ResponseEntity<String> getSnowflakeId(@PathVariable("key") String key) {
        try {
            return ResponseEntity.ok(String.valueOf(idGenerator.getSnowflakeId(key)));
        } catch (NoSuchKeyException ignored) {
            return ResponseEntity.badRequest().build();
        } catch (IdGenerateException ignored) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }
}
