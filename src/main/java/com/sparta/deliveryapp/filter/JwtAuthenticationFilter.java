package com.sparta.deliveryapp.filter;

import com.sparta.deliveryapp.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
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

        String url = httpRequest.getRequestURI();


        //가입, 로그인은 jwt 체크 불필요
        if (url.startsWith("/members/owner/sign-up") || url.startsWith("/members/user/sign-up")|| url.startsWith("/members/sign-in")) {
            chain.doFilter(request, response);
            return;
        }

        // 정적 리소스에 대한 요청은 JWT 검증을 생략
        if (url.startsWith("/images/") || url.startsWith("/css/") || url.startsWith("/js/") || url.startsWith("/webjars/")) {
            chain.doFilter(request, response);
            return;
        }

        // 헤더에서 Authorization 토큰을 가져옵니다.
        String authorizationHeader = httpRequest.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 필요합니다.");
            return;
        }

        // 헤더에서 JWT 추출
        String token = jwtUtil.getJwtFromHeader(httpRequest);

        try {
            Claims claims = jwtUtil.extractClaims(token);

            // 토큰을 검증하고, 유효한 경우 필터 체인을 타고 다음 필터로 이동합니다.
            if (jwtUtil.validateToken(token)){
                // 필요하다면 요청에 사용자 정보를 추가할 수 있습니다.
                httpRequest.setAttribute("id", Integer.parseInt(claims.getSubject()));
                httpRequest.setAttribute("username", (claims.get("username", String.class)));
                httpRequest.setAttribute("role", claims.get("role", String.class));

                chain.doFilter(request, response);
            } else {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 유효하지 않습니다.");
            }
        } catch (Exception e) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰 검증 중 오류가 발생했습니다.");
        }
    }
}
