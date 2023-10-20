package io.vzhidu.chassis.common.spring.web;

import io.vzhidu.chassis.common.spring.web.http.ServletErrorController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static io.vzhidu.chassis.common.spring.web.ErrorResponseAssertion.*;
import static io.vzhidu.chassis.common.spring.web.ErrorResponseAssertion.assertResponse;

/**
 * Test for {@link ServletErrorController}
 *
 * @author Zhiqiang Du Created at 2020/3/23
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = AbstractSpringBootApplication.class,
        properties = {"server.error.include-exception=true", "server.error.include-message=always"}
)
class ServletErrorControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void test_404() throws IOException {
        ResponseEntity<String> res = restTemplate.getForEntity("/mock/not_exist_path", String.class);
        assertResponse(NO_HANDLER_EXCEPTION, HttpStatus.NOT_FOUND, res, objectMapper);
    }

    @Test
    void test_500() throws IOException {
        ResponseEntity<String> res = restTemplate.getForEntity("/mock/ex", String.class);
        assertResponse(EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR, res, objectMapper);
    }
}
