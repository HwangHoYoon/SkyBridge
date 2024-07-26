package com.skybridge.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestFilter implements Filter {

    private final MultipartResolver multipartResolver;

    @Value("${web.filter.url}")
    private String[] urls;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //chain.doFilter(request, response);
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);

        if (multipartResolver.isMultipart((HttpServletRequest) request)) {
            // 멀티파트 요청으로부터 MultipartHttpServletRequest 획득
            MultipartHttpServletRequest multipartRequest = multipartResolver.resolveMultipart((HttpServletRequest) request);

            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                MultipartFile file = entry.getValue();
                // 파일 데이터를 다시 설정
                byte[] fileBytes = file.getBytes(); // 파일 데이터 읽기
                requestWrapper.setAttribute(entry.getKey(), fileBytes); // 새로운 요청(wrapper)에 파일 데이터 설정
            }
        }

        CustomRequestWrapper customRequestWrapper = new CustomRequestWrapper(requestWrapper);

        HttpServletResponseWrapper responseWrapper;
        String responseBody = "";
        int status = 0;

        if (Arrays.stream(urls).noneMatch(customRequestWrapper.getRequestURI()::contains)) {
            //responseWrapper = new CustomHttpServletResponseWrapper((HttpServletResponse) response);
            responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);
            chain.doFilter(customRequestWrapper, responseWrapper);

            responseBody = getResponseBody(responseWrapper);
            status = responseWrapper.getStatus();
        } else {
            chain.doFilter(customRequestWrapper, response);
        }

        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();

        if (!customRequestWrapper.getRequestURI().contains("/api-docs/") && !customRequestWrapper.getRequestURI().contains("/swagger-ui/")) {
            if (Arrays.stream(urls).noneMatch(customRequestWrapper.getRequestURI()::contains)) {
                log.info("\n" +
                                "[REQUEST] {} - {} {} - {}\n" +
                                "Headers : {}\n" +
                                "Request : {}\n" +
                                "Response : {}\n",
                        ((HttpServletRequest) customRequestWrapper).getMethod(),
                        ((HttpServletRequest) customRequestWrapper).getRequestURI(),
                        status,
                        (end - start) / 1000.0,
                        getHeaders(customRequestWrapper),
                        buildAccessLog(customRequestWrapper),
                        responseBody);
            } else {
                log.info("\n" +
                                "[REQUEST] {} - {} {} - {}\n" +
                                "Headers : {}\n" +
                                "Request : {}\n" ,
                        ((HttpServletRequest) customRequestWrapper).getMethod(),
                        ((HttpServletRequest) customRequestWrapper).getRequestURI(),
                        status,
                        (end - start) / 1000.0,
                        getHeaders(customRequestWrapper),
                        buildAccessLog(customRequestWrapper));
            }
        } else {
            log.info("[REQUEST] {} - {} {} - {}", ((HttpServletRequest) customRequestWrapper).getMethod(), ((HttpServletRequest) customRequestWrapper).getRequestURI(), status, (end - start) / 1000.0);
        }
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap<>();

        Enumeration<String>  headerArray = request.getHeaderNames();
        while (headerArray.hasMoreElements()) {
            String headerName = headerArray.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

    private Map<String, String> getHeaders(final HttpServletResponse response) {
        Map<String, String> headerMap = new HashMap<>();
        response.getHeaderNames().forEach(s -> {
            headerMap.put(s, response.getHeader(s));
        });
        return headerMap;
    }


    private String getResponseBody(final HttpServletResponse response) throws IOException {
        String payload = null;
        ContentCachingResponseWrapper wrapper =
                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                payload = new String(buf, 0, buf.length, StandardCharsets.UTF_8);
                wrapper.copyBodyToResponse();
            }
        }
        return null == payload ? " - " : payload;
    }

    private String buildAccessLog(CustomRequestWrapper customRequestWrapper) {

        try {
            String requestURL = getRequestURL(customRequestWrapper);
            String remoteAddr = getRemoteAddr(customRequestWrapper);
            String method = getMethod(customRequestWrapper);
            String queryString = getQueryString(customRequestWrapper);
            String requestBody = getRequestBody(customRequestWrapper);

            StringBuilder sb = new StringBuilder();
            sb.append("{");
            if (requestURL != null) {
                sb
                        .append("\"").append("requestURL").append("\"")
                        .append(":")
                        .append("\"").append(requestURL).append("\"");
            }
            if (remoteAddr != null) {
                sb
                        .append(",")
                        .append("\"").append("remoteAddr").append("\"")
                        .append(":")
                        .append("\"").append(remoteAddr).append("\"");
            }
            if (method != null) {
                sb
                        .append(",")
                        .append("\"").append("method").append("\"")
                        .append(":")
                        .append("\"").append(method).append("\"");
            }
            if (queryString != null) {
                sb
                        .append(",")
                        .append("\"").append("queryString").append("\"")
                        .append(":")
                        .append("\"").append(queryString).append("\"");
            }
            if (requestBody != null && !requestBody.isEmpty()) {
                sb
                        .append(",")
                        .append("\"").append("body").append("\"")
                        .append(":")
                        .append("\"").append(requestBody).append("\"");
            }
            sb.append("}");
            return sb.toString();
        } catch (Exception e) {
            log.error("buildAccessLog Exception : {}", e.getMessage());
        }
        return null;
    }

    private String getRequestBody(CustomRequestWrapper customRequestWrapper) {
        String content = null;
        String method = customRequestWrapper.getMethod().toLowerCase();

        // POST, PUT + application/json
        if (method.startsWith("p")) {
            if (customRequestWrapper.getContentType().toLowerCase().indexOf("json") > 0) {
                try {
                    content = new String(customRequestWrapper.getBody(), customRequestWrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    log.error(e.getMessage());
                }
            }
        }
        return content;
    }

    private String getQueryString(CustomRequestWrapper customRequestWrapper) throws UnsupportedEncodingException {
        String queryString = null;
        if (customRequestWrapper.getQueryString() != null) {
            queryString = URLDecoder.decode(customRequestWrapper.getQueryString(), StandardCharsets.UTF_8);
        }
        return queryString;
    }

    private String getMethod(CustomRequestWrapper customRequestWrapper) {
        return customRequestWrapper.getMethod();
    }

    private String getRemoteAddr(CustomRequestWrapper customRequestWrapper) {
        return customRequestWrapper.getHeader("X-Forwarded-For") == null ? customRequestWrapper.getRemoteAddr() : customRequestWrapper.getHeader("X-Forwarded-For");
    }

    private String getRequestURL(CustomRequestWrapper customRequestWrapper) {
        return customRequestWrapper.getRequestURL().toString();
    }
}