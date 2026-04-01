package br.com.munnincrow.api.dto;

public class LoginResponse {
    public String token;

    public LoginResponse(String token) {
        this.token = token;
    }
}