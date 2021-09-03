package io.vzhidu.chassis.common.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation to mark classes for experimental use.
 *
 * <p>Classes with this annotation are neither battle-tested nor stable, and may be changed or removed
 * in future versions.
 *
 * <p>This annotation also excludes classes with evolving interfaces / signatures
 * annotated with {@link Public} and {@link PublicEvolving}.
 *
 * @author Zhiqiang Du Created at 2019/12/19
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR })
@Public
public @interface Experimental {
}
