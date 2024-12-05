package com.example.demo.service;

import reactor.core.publisher.Mono;

public interface FlaskService {

    public String sendMessageToFlask(String message);
}
