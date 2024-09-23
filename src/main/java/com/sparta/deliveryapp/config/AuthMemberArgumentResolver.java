package com.sparta.deliveryapp.config;

import com.sparta.deliveryapp.annotation.Auth;
import com.sparta.deliveryapp.domain.member.UserRole;
import com.sparta.deliveryapp.domain.member.dto.AuthMember;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(Auth.class) != null;
    }

    // AuthUser 객체를 생성하여 반환
    @Override
    public Object resolveArgument(
            @Nullable MethodParameter parameter,
            @Nullable ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            @Nullable WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        // JwtFilter 에서 set 한 userId, username, userRole 값을 가져옴
        Integer id = (Integer) request.getAttribute("id");
        String username = (String) request.getAttribute("username");
        String roleStr = (String) request.getAttribute("role");
        Boolean isActive = (Boolean) request.getAttribute("isActive");
        Boolean isSecession = (Boolean) request.getAttribute("isSecession");

        UserRole role = UserRole.valueOf(roleStr);
        return new AuthMember(id, username, role, isActive, isSecession);
    }
}
