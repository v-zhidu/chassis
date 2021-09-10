package io.vzhidu.chassis.middleware.core.idgenerator.ex;

import io.vzhidu.chassis.common.core.ex.ResourceNotFoundException;

/**
 * 业务key值不存在
 *
 * @author Zhiqiang Du Created at 2020/8/21
 */
public class NoSuchKeyException extends ResourceNotFoundException {

    private static final long serialVersionUID = 1048811499985242418L;

    /**
     * 构造函数
     *
     * @param key invalid key name
     */
    public NoSuchKeyException(String key) {
        super("Key is not exist, key: " + key);
    }
}
