package com.patika.paycore.security;

import com.patika.paycore.exception.ApiErrorType;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {

        JSONObject responseJson = new JSONObject();
        responseJson.put("errorCode", String.valueOf(ApiErrorType.AUTHENTICATION_ERROR.getErrorCode()));
        responseJson.put("errorMessage", ApiErrorType.AUTHENTICATION_ERROR.getErrorMessage());
        responseJson.put("errorHttpStatus", String.valueOf(ApiErrorType.AUTHENTICATION_ERROR.getHttpStatus()));

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println(responseJson.toString());

    }
}