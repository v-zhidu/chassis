package io.vzhidu.chassis.middleware.core.idgenerator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 业务标识
 */
@Getter
@Setter
@ToString
public class BizTag {

    /**
     * 业务代码
     */
    private String key;

    /**
     * 最大ID
     */
    private long maxId;

    /**
     * 步长
     */
    private long step;

    /**
     * 更新时间
     */
    private String updateTime;
}
