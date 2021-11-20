package io.vzhidu.chassis.common.spring.web.http;

import org.apache.commons.text.CaseUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 将请求参数中的snake_case转为camel_case
 *
 * @author Zhiqiang Du created at 2021/10/22
 */
public class SnakeCaseRequestQueryConverter extends OncePerRequestFilter {

    /**
     * Filter each request and transfer the query parameters
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final Map<String, String[]> parameters = new ConcurrentHashMap<>(1);

        for (String param : request.getParameterMap().keySet()) {
            String camelCaseParam = CaseUtils.toCamelCase(param, false, '_');

            parameters.put(camelCaseParam, request.getParameterValues(param));
            parameters.put(param, request.getParameterValues(param));
        }

        filterChain.doFilter(new HttpServletRequestWrapper(request) {
            @Override
            public String getParameter(String name) {
                return parameters.containsKey(name) ? parameters.get(name)[0] : null;
            }

            @Override
            public Enumeration<String> getParameterNames() {
                return Collections.enumeration(parameters.keySet());
            }

            @Override
            public String[] getParameterValues(String name) {
                return parameters.get(name);
            }

            @Override
            public Map<String, String[]> getParameterMap() {
                return parameters;
            }
        }, response);
    }
}
