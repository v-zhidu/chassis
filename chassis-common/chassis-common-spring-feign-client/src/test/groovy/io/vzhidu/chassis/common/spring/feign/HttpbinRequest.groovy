package io.vzhidu.chassis.common.spring.feign

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class HttpbinRequest {

    @NotNull
    @Size(min = 1, max = 10)
    String name
}
