package io.vzhidu.chassis.middleware.core.idgenerator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 业务标识详细信息
 */
@Getter
@Setter
@ToString
public class BizTagDetail {

    /**
     * 业务代码
     */
    private String key;

    /**
     * 是否初始化成功
     */
    private boolean initOk;

    /**
     * 下一号段是否准备好
     */
    private boolean nextReady;

    /**
     * 当前使用号段位置
     */
    private int position;

    /**
     * 下一个发号的id
     */
    private long value0;
    /**
     * 步长
     */
    private int step0;
    /**
     * 最大值
     */
    private long max0;

    /**
     * 下一个发号的id
     */
    private long value1;
    /**
     * 步长
     */
    private int step1;
    /**
     * 最大值
     */
    private long max1;
}
