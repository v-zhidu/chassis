package io.vzhidu.chassis.common.core.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Abstract Domain Entity include basic fields
 *
 * @param <T> Generic type of field id
 * @author Zhiqiang Du Created at 2020/4/9
 */
@Getter
@Setter
@ToString
public abstract class AbstractEntity<T extends Serializable> implements Identifiable<T> {

    /**
     * Unique identifier for locate a unique record
     */
    private T id;

    /**
     * The millisecond that entity be created
     */
    private LocalDateTime createAt;

    /**
     * The millisecond that entity be last update
     */
    private LocalDateTime updateAt;

    /**
     * Whether this entity has been delete
     */
    private transient Boolean deleted;

    /**
     * Default constructor, only invoke by his subclass
     */
    protected AbstractEntity() {
    }
}
