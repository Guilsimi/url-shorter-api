package com.example.ls_api.services.exceptions;

public class UrlShortenerException extends RuntimeException {
    private final String url;

    public UrlShortenerException(String message, String url) {
        super(message);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
