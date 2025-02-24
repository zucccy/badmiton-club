package com.yun.springbootinit.aop;

import com.yun.springbootinit.annotation.AuthCheck;
import com.yun.springbootinit.common.ErrorCode;
import com.yun.springbootinit.exception.BusinessException;
import com.yun.springbootinit.model.entity.User;
import com.yun.springbootinit.model.enums.UserRoleEnum;
import com.yun.springbootinit.service.IUserService;
import com.yun.springbootinit.utils.UserContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 权限校验 AOP
 *
 * @author chenyun
 * 
 */
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private IUserService userService;
    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param authCheck
     * @return
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        // 先走拦截器，必有当前登录用户
        User loginUser = UserContextHolder.getLoginUser();
        // 必须有该权限才通过
        if (StringUtils.isNotBlank(mustRole)) {
            UserRoleEnum mustUserRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
            if (mustUserRoleEnum == null) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
            String userRole = loginUser.getRole();
            // 如果被封号，直接拒绝
            if (UserRoleEnum.BAN.equals(mustUserRoleEnum)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
            // 必须有管理员权限
            if (UserRoleEnum.ADMIN.equals(mustUserRoleEnum)) {
                if (!StringUtils.equals(mustRole, userRole)) {
                    throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
                }
            }
        }
        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}

