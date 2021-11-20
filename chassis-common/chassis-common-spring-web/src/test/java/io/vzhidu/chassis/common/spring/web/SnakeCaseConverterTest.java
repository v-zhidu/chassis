package io.vzhidu.chassis.common.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vzhidu.chassis.common.core.rpc.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static io.vzhidu.chassis.common.spring.web.ErrorResponseAssertion.assertResponse;

/**
 * {Description}
 *
 * @author Zhiqiang Du created at 2021/10/25
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AbstractSpringBootApplication.class)
public class SnakeCaseConverterTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testBindException(@Autowired TestRestTemplate restTemplate) throws IOException {
        ResponseEntity<String> res = restTemplate.getForEntity("/mock/snake_case?user_id=1", String.class);
        assertResponse(Response.ok("1"), HttpStatus.OK, res, objectMapper);
    }
}
