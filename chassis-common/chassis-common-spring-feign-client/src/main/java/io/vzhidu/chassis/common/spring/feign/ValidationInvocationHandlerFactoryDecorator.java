package io.vzhidu.chassis.common.spring.feign;

import feign.InvocationHandlerFactory;
import feign.Target;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Set;

/**
 * 通过反射校验参数和返回值是否合法
 *
 * @author Zhiqiang Du created at 2021/10/3
 */
public class ValidationInvocationHandlerFactoryDecorator implements InvocationHandlerFactory {

    private final InvocationHandlerFactory original;
    private final Validator validator;
    private Object targetTypeInstance;

    /**
     * 构造函数
     */
    public ValidationInvocationHandlerFactoryDecorator(final InvocationHandlerFactory original,
                                                       final Validator validator) {
        this.original = original;
        this.validator = validator;
    }

    /**
     * A decorator, which triggers validation.
     */
    private class MethodHandlerDecorator implements MethodHandler {

        private final Method method;
        private final MethodHandler methodHandler;

        MethodHandlerDecorator(final Method method, final MethodHandler methodHandler) {
            this.method = method;
            this.methodHandler = methodHandler;
        }

        @Override
        public Object invoke(final Object[] argv) throws Throwable {
            Set<ConstraintViolation<Object>> constraintViolationSet;
            // 检查参数
            if (argv != null && argv.length > 0) {
                constraintViolationSet = validator.forExecutables()
                        .validateParameters(targetTypeInstance, method, argv);
                if (!constraintViolationSet.isEmpty()) {
                    throw new ConstraintViolationException(constraintViolationSet);
                }
            }

            final Object response = methodHandler.invoke(argv);

            // 检查返回值
            constraintViolationSet = validator.forExecutables()
                    .validateReturnValue(targetTypeInstance, method, response);
            if (!constraintViolationSet.isEmpty()) {
                throw new ConstraintViolationException(constraintViolationSet);
            }

            return response;
        }
    }

    /**
     * 创建{@link InvocationHandler}对象
     */
    @Override
    public InvocationHandler create(final Target target, final Map<Method, MethodHandler> dispatch) {
        try {
            this.targetTypeInstance = createTargetInstance(target.type());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        for (final Map.Entry<Method, MethodHandler> entry : dispatch.entrySet()) {
            entry.setValue(new MethodHandlerDecorator(entry.getKey(), entry.getValue()));
        }
        return original.create(target, dispatch);
    }

    private Object createTargetInstance(final Class type) throws InstantiationException, IllegalAccessException {
        return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, (o, method, objects) -> {
            if ("hashCode".equals(method.getName())) {
                return hashCode();
            } else if ("toString".equals(method.getName())) {
                return toString();
            } else {
                return null;
            }
        });
    }
}
