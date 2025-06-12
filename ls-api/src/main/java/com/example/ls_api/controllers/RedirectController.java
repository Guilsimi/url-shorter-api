package com.example.ls_api.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ls_api.model.entities.ShortenedUrlEntity;
import com.example.ls_api.services.AccessInfoService;
import com.example.ls_api.services.ShortenedUrlService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping(value = "/li")
public class RedirectController {

    @Autowired
    private AccessInfoService accessService;

    @Autowired
    private ShortenedUrlService urlService;

    @Transactional
    @GetMapping("/{id}")
    public void redirectUrl(@PathVariable("id") String id,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        accessService.insertAccessInfoData(request);

        ShortenedUrlEntity entity = urlService.findById(id,
                "Invalid url", request.getRequestURI());

        urlService.updateClicksNumber(entity);

        response.sendRedirect(entity.getOriginalUrl());
    }

}
