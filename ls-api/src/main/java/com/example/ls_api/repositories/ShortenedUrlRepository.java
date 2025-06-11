package com.example.ls_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ls_api.model.entities.ShortenedUrlEntity;

public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrlEntity, String> {

    public Optional<ShortenedUrlEntity> findByOriginalUrl(String originalUrl);

}
