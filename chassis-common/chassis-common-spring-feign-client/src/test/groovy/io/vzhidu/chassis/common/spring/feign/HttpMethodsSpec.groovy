package io.vzhidu.chassis.common.spring.feign

import io.vzhidu.chassis.common.spring.web.AbstractSpringBootApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import javax.validation.ConstraintViolationException

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = AbstractSpringBootApplication.class,
        properties = "logging.level.io.vzhidu.chassis.common.spring.feign=DEBUG"
)
class HttpMethodsSpec extends Specification {

    @Autowired
    HttpMethods httpMethods

    def "test get request"(Map parameter) {
        when:
        def response = httpMethods.get(parameter)

        then:
        response.url == url
        and:
        response.args == parameter

        where:
        parameter                     | url
        ["id": "1"]                   | "https://httpbin.org/get?id=1"
        ["name": "Jack", "age": "18"] | "https://httpbin.org/get?name=Jack&age=18"
    }

    def "test post request"(Object obj) {
        when:
        def response = httpMethods.post(obj)

        then:
        response.url == url
        and:
        response.json == obj
        and:
        response.data == data

        where:
        obj                         | data                             | url
        [1, 2, 3]                   | "[1,2,3]"                        | "https://httpbin.org/post"
        ["name": "Jack", "age": 18] | "{\"name\":\"Jack\",\"age\":18}" | "https://httpbin.org/post"
    }

    def "test request interceptor"() {
        when:
        def response = httpMethods.get(parameter)

        then:
        response.headers.containsKey("X-Trace-Id")

        where:
        parameter   | url
        ["id": "1"] | "https://httpbin.org/get?id=1"
    }

    def "test okhttp interceptor"() {
        when:
        def response = httpMethods.getOriginalResponse(parameter)

        then:
        response.headers().containsKey("X-Timestamp")

        where:
        parameter   | url
        ["id": "1"] | "https://httpbin.org/get?id=1"
    }

    def "validate request parameter validate"(HttpbinRequest request) {
        when:
        httpMethods.postValidate(request)

        then:
        def e = thrown(ConstraintViolationException)
        e.message == "postValidate.arg0.name: size must be between 1 and 10"

        where:
        request                                   | _
        new HttpbinRequest(name: "0123456789abc") | _
        __
    }
}
