package com.yun.springbootinit.utils;

/**
 * @Description: TODO
 * @Author: chenyun
 * @Date: 2025/2/26 22:49
 * @version: 1.0
 */

import javax.validation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ValidatorUtils {
    private static final Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * 执行校验并抛出包含所有错误信息的异常
     */
    public static void validate(Object object) {
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<Object> violation : violations) {
                errorMessages.add(violation.getMessage());
            }
            throw new ValidationException(String.join("; ", errorMessages));
        }
    }
}
