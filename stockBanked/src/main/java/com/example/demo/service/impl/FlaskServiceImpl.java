package com.example.demo.service.impl;

import com.example.demo.service.FlaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FlaskServiceImpl implements FlaskService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String FLASK_URL = "http://localhost:5000/hello";
    private static final String CONTENT_TYPE_JSON = "application/json";
    // 请求头
    HttpHeaders headers = createHeaders();
    // 创建请求头
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", CONTENT_TYPE_JSON);
        return headers;
    }
    // 构建请求体
    private String buildRequestBody(String message) {
        return "{\"message\": \"" + message + "\"}";
    }
    // 发送 POST 请求
    private String sendPostRequest(String url, HttpEntity<String> entity) {
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }

    public String sendMessageToFlask(String message) {
        // 请求体
        String body = buildRequestBody(message);

        // 创建 HttpEntity，包含请求头和请求体
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        // 发送 POST 请求
        return sendPostRequest(FLASK_URL, entity);
    }
}

