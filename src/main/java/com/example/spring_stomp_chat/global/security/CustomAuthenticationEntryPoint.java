package com.example.spring_stomp_chat.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");

        // 에러 메시지 설정
        if (exception == null) {
            setResponse(response, "UNKNOWN_ERROR");
        } else if (exception.equals("EXPIRED_TOKEN")) {
            setResponse(response, "EXPIRED_TOKEN");
        } else if (exception.equals("INVALID_TOKEN")) {
            setResponse(response, "INVALID_TOKEN");
        }
    }

    private void setResponse(HttpServletResponse response, String errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 1. 응답 내용을 담을 Map 생성
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", "인증에 실패했습니다.");
        responseMap.put("code", errorCode);

        // 2. Jackson의 ObjectMapper를 사용하여 Map을 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String responseJson = objectMapper.writeValueAsString(responseMap);

        // 3. 응답 출력
        response.getWriter().print(responseJson);
    }
}
