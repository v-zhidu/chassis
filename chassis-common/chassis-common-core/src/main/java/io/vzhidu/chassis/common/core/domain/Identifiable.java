package io.vzhidu.chassis.common.core.domain;

import java.io.Serializable;

/**
 * A class used to mark class has a identifiable unique id
 *
 * @param <T> the type of unique key
 * @author Zhiqiang Du Created at 2019/9/24
 */
public interface Identifiable<T extends Serializable> {

    /**
     * Return the unique id of entity
     *
     * @return the unique id
     */
    T getId();
}
