package io.vzhidu.chassis.common.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Interface to mark methods within stable, public APIs as an internal developer API.
 *
 * <p>Developer APIs are stable but internal to project and might change across releases.
 *
 * @author Zhiqiang Du Created at 2019/12/19
 */
@Documented
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR })
@Public
public @interface Internal {
}
