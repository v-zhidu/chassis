package io.vzhidu.chassis.common.spring.feign;

import feign.RequestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Util methods of {@link feign.Feign}
 *
 * @author Zhiqiang Du created at 2021/9/23
 */
public final class FeignUtil {

    /**
     * 替换url中的query parameter
     * 例如：
     * url: https://example.com
     * key: id
     * values: 1, 2
     * <p>
     * result: https://example.com?id=1&id=2
     *
     * @param template {@link RequestTemplate}
     * @param key      键
     * @param value    值
     */
    public static void clearAndSetQuery(RequestTemplate template, String key, String value) {
        if (value != null && value.length() > 0) {
            template.query(key, Collections.emptyList());
            template.query(key, value);
        }
    }

    /**
     * 获取url中特定key的值
     *
     * @param key 键
     * @return 值
     */
    public static Optional<String> query(RequestTemplate template, String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Query key is null or empty");
        }

        Map<String, Collection<String>> queries = template.queries();
        if (queries.isEmpty() || !queries.containsKey(key)) {
            return Optional.empty();
        }

        return queries.get(key).stream().findFirst();
    }

    /**
     * 解析RequestTemplate中的path
     * template.path() 有时返回/api/v2, 有时返回https://example.com/api/v2
     *
     * @param template {@link RequestTemplate}
     * @return RequestTemplate中的path
     * @throws URISyntaxException url解析异常
     */
    public static String resolvePath(RequestTemplate template) throws URISyntaxException {
        return new URI(template.path()).getPath();
    }
}
