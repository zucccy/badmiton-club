package com.yun.springbootinit.exception;

import com.yun.springbootinit.common.BaseResponse;
import com.yun.springbootinit.common.ErrorCode;
import com.yun.springbootinit.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;
import java.util.Optional;

/**
 * 全局异常处理器
 *
 * @author chenyun
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException: ", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException: ", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }

    @ExceptionHandler(BindException.class)
    public BaseResponse<?> bindExceptionHandler(BindException e) {
        log.error("BindException: ", e);
        String errorMessage = Optional.ofNullable(e.getBindingResult().getFieldError()).map(FieldError::getDefaultMessage).orElse(ErrorCode.PARAMS_ERROR.getMessage());
        int errorCode = ErrorCode.PARAMS_ERROR.getCode();
        log.error("errorMessage: {}", errorMessage);
        return ResultUtils.error(errorCode, errorMessage);
    }
}
