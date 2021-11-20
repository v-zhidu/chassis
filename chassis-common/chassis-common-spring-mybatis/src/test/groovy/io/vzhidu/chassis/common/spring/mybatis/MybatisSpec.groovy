package io.vzhidu.chassis.common.spring.mybatis

import io.vzhidu.chassis.common.spring.mybatis.mapper.UserMapper
import io.vzhidu.chassis.common.spring.web.AbstractSpringBootApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AbstractSpringBootApplication)
class MybatisSpec extends Specification {

    @Autowired
    UserMapper mapper

    def "test mybatis save"(long id, String name) {
        given:
        User user = new User()
        user.id = id
        user.name = name
        User userInDb = null

        when:
        if (mapper.save(user)) {
            userInDb = mapper.findById(id)
        }

        then:
        userInDb != null
        userInDb.id == id
        userInDb.name == name
        userInDb.createAt != null
        !userInDb.deleted

        where:
        id | name
        1L | "Jack"
    }
}
