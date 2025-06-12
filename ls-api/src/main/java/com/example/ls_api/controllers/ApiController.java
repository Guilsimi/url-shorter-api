package com.example.ls_api.controllers;

import java.util.List;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ls_api.model.dto.ShortenedUrlDTO;
import com.example.ls_api.model.dto.UrlShortenRequest;
import com.example.ls_api.model.dto.UrlShortenResponse;
import com.example.ls_api.model.entities.ShortenedUrlEntity;
import com.example.ls_api.model.mappers.ShortenUrlMapper;
import com.example.ls_api.services.ShortenedUrlService;
import com.example.ls_api.services.exceptions.UrlShortenerException;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api")
public class ApiController {

    @Autowired
    private ShortenedUrlService urlService;

    @Autowired
    private ShortenUrlMapper mapper;

    @PostMapping(value = "/shorten")
    public ResponseEntity<UrlShortenResponse> shortenUrl(
            @RequestBody UrlShortenRequest shortenRequest,
            HttpServletRequest request) {

        String url = verifyScheme(shortenRequest.url());

        ShortenedUrlEntity entity = urlService.findByOriginalUrl(url);

        if (entity != null) {
            UrlShortenResponse response = mapper.shortenedUrlToResponseDto(entity);
            return ResponseEntity.ok().body(response);
        }

        if (!isValidUrl(url)) {
            throw new UrlShortenerException(
                    "Invalid url format, submit a valid url!!",
                    url);
        }

        UrlShortenResponse response = mapper
                .shortenedUrlToResponseDto(
                        urlService.createShortenedUrl(url, request));

        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "/links")
    public ResponseEntity<List<ShortenedUrlDTO>> getUrlsList() {
        return ResponseEntity.ok().body(mapper.shortenedUrlToDTO(urlService.findAll()));
    }

    private boolean isValidUrl(String url) {
        if (!url.contains("://")) {
            return new UrlValidator().isValid("http://" + url);
        }

        return new UrlValidator().isValid(url);
    }

    private String verifyScheme(String url) {
        if (!url.contains("://")) {
            return "http://" + url;
        }
        return url;
    }

}
