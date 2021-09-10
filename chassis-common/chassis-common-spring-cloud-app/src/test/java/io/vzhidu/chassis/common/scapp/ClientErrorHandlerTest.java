package io.vzhidu.chassis.common.scapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

import static io.vzhidu.chassis.common.scapp.ErrorResponseAssertion.*;

/**
 * Test for {@link ClientErrorHandlerTest}
 *
 * @author Zhiqiang Du Created at 2019/12/30
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientErrorHandlerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testMediaTypeException() throws IOException {
        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders().add("Content-Type", "text/plain");
                    return execution.execute(request, body);
                })
        );
        ResponseEntity<String> res = restTemplate.postForEntity("/mock/post", null, String.class);

        assertResponse(MEDIA_TYPE_EXCEPTION, HttpStatus.BAD_REQUEST, res, objectMapper);
    }

    @Test
    public void testMissingServletRequestParameterException() throws IOException {
        ResponseEntity<String> res = restTemplate.getForEntity("/mock/get", String.class, "");
        assertResponse(MISSING_REQUEST_PARAMETER_EXCEPTION, HttpStatus.BAD_REQUEST, res, objectMapper);
    }

    @Test
    public void testHttpMessageNotReadableException() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(restTemplate.getRootUri() + "/mock/post");
        CloseableHttpResponse response = client.execute(post);
        String res = EntityUtils.toString(response.getEntity());
        System.out.println(res);
        assertResponse(REQUEST_BODY_NOT_READABLE_EXCEPTION, HttpStatus.BAD_REQUEST,
                new ResponseEntity<>(
                        res, Objects.requireNonNull(HttpStatus.resolve(response.getStatusLine().getStatusCode()))), objectMapper);
    }

    @Test
    public void testInvalidFormatException(@Autowired TestRestTemplate restTemplate) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = "{\"id\": \"1\",\"type\": \"INVALID_TYPE\",\"name\": \"fake_user\"}";
        HttpEntity<String> httpEntity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> res = restTemplate.postForEntity("/mock/post", httpEntity, String.class);
        assertResponse(INVALID_FORMAT_EXCEPTION, HttpStatus.BAD_REQUEST, res, objectMapper);
    }

    @Test
    public void testInvalidFormatExceptionWithInvalidDate(@Autowired TestRestTemplate restTemplate) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = "{\"id\": \"1\",\"type\": \"NORMAL\",\"name\": \"fake_user\",\"birth_time\": \"2020-01-01\"}";
        HttpEntity<String> httpEntity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> res = restTemplate.postForEntity("/mock/post", httpEntity, String.class);
        assertResponse(INVALID_FORMAT_EXCEPTION_DATE, HttpStatus.BAD_REQUEST, res, objectMapper);
    }

    @Test
    public void testInvalidParameterException() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = "{\"id\": \"1\",\"type\": \"MOCK\",\"name\": \"Jack\"}";

        HttpEntity<String> httpEntity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> res = restTemplate.postForEntity("/mock/post", httpEntity, String.class);
        assertResponse(INVALID_PARAMETER_EXCEPTION, HttpStatus.BAD_REQUEST, res, objectMapper);
    }

    @Test
    public void testBindException(@Autowired TestRestTemplate restTemplate) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = "{\"id\": \"1\",\"type\": \"NORMAL\",\"name\": \"fake_user_has_valid_length\"}";
        HttpEntity<String> httpEntity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> res = restTemplate.postForEntity("/mock/post", httpEntity, String.class);
        assertResponse(BINDING_EXCEPTION, HttpStatus.BAD_REQUEST, res, objectMapper);
    }

    @Test
    public void testPermissionDeniedException() throws IOException {
        ResponseEntity<String> res = restTemplate.getForEntity("/mock/get?id=0", String.class);
        assertResponse(PERMISSION_DENIED, HttpStatus.FORBIDDEN, res, objectMapper);
    }

    @Test
    public void testRequestMethodNotSupportException() throws IOException {
        ResponseEntity<String> res = restTemplate.postForEntity("/mock/get", null, String.class);
        assertResponse(METHOD_NOT_SUPPORT, HttpStatus.NOT_FOUND, res, objectMapper);
    }

    @Test
    public void testResourceNotFoundException() throws IOException {
        ResponseEntity<String> res = restTemplate.getForEntity("/mock/get?id=-1", String.class);
        assertResponse(RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND, res, objectMapper);
    }
}
