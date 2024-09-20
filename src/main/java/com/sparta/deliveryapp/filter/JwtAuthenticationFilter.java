package com.sparta.deliveryapp.filter;

import com.sparta.deliveryapp.jwt.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter implements Filter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        // 초기화 로직이 필요한 경우 구현
//    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 헤더에서 JWT 추출
        String token = jwtUtil.getJwtFromHeader(httpRequest);

        // 토큰이 유효한지 검증
        if (token != null && jwtUtil.validateToken(token)) {
            // 유효한 경우 다음 필터로 진행
            chain.doFilter(request, response);
        } else {
            // 유효하지 않다면 401 에러 반환
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Invalid or missing JWT token");
        }
    }
}
