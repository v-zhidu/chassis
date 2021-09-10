package io.vzhidu.chassis.common.scapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vzhidu.chassis.common.scapp.http.ServletErrorController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static io.vzhidu.chassis.common.scapp.ErrorResponseAssertion.*;

/**
 * Test for {@link ServletErrorController}
 *
 * @author Zhiqiang Du Created at 2020/3/23
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServletErrorControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void testNoHandlerException() throws IOException {
        ResponseEntity<String> res = restTemplate.getForEntity("/mock/not_exist_path", String.class);
        assertResponse(NO_HANDLER_EXCEPTION, HttpStatus.NOT_FOUND, res, objectMapper);
    }

    @Test
    public void testException() throws IOException {
        ResponseEntity<String> res = restTemplate.getForEntity("/mock/ex", String.class);
        assertResponse(EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR, res, objectMapper);
    }
}
