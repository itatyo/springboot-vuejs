package dev.itatyo.demo.restservice.controller;

import lombok.Value;

@Value
public class ErrorResponse {
    String errorCode;
    String message;
}
