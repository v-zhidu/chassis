package io.vzhidu.chassis.common.scof;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * {Description}
 *
 * @author Zhiqiang Du created at 2021/5/12
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FeignClientTest {

    @Autowired
    Github github;

    @Test
    void test() {
        // Fetch and print a list of the contributors to this library.
        List<Github.Contributor> contributors = github.contributors("OpenFeign", "feign");
        Assertions.assertFalse(contributors.isEmpty());
    }
}