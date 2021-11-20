package io.vzhidu.chassis.common.spring.feign;

import feign.Feign;
import feign.InvocationHandlerFactory;

import javax.validation.Validator;

/**
 * 对原生{@link Feign.Builder}的增强, 支持{@link Validator}
 * <p>
 *
 * @author Zhiqiang Du created at 2021/10/3
 */
public abstract class ValidationFeign {

    /**
     * 创建默认的{@link Builder}
     */
    public static Builder builder(Validator validator) {
        return new Builder(validator);
    }

    public static class Builder extends Feign.Builder {

        private final Validator validator;

        public Builder(Validator validator) {
            this.validator = validator;
        }

        /**
         * 覆盖{@link Feign.Builder#invocationHandlerFactory(InvocationHandlerFactory)}
         */
        @Override
        public Feign.Builder invocationHandlerFactory(InvocationHandlerFactory invocationHandlerFactory) {
            if (validator != null) {
                return super.invocationHandlerFactory(
                        new ValidationInvocationHandlerFactoryDecorator(invocationHandlerFactory, validator));
            } else {
                return super.invocationHandlerFactory(invocationHandlerFactory);
            }
        }
    }
}
