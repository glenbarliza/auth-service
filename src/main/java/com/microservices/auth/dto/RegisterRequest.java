package com.microservices.auth.dto;

import java.util.List;

public record RegisterRequest(String name, String email, String password, List<String> roles) {}
