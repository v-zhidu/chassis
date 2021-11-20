package io.vzhidu.chassis.common.spring.mybatis.mapper

import io.vzhidu.chassis.common.spring.mybatis.BaseMapper
import io.vzhidu.chassis.common.spring.mybatis.User

interface UserMapper extends BaseMapper<User> {

    boolean save(User user)

    User findById(long id)
}
