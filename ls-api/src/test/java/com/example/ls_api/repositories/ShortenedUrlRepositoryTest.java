package com.example.ls_api.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.ls_api.model.dto.ShortenedUrlDTO;
import com.example.ls_api.model.entities.ShortenedUrlEntity;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
public class ShortenedUrlRepositoryTest {

    @Autowired
    ShortenedUrlRepository repository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should find a ShortenedUrlEntity in DB by original URL")
    public void findByOriginalUrlCaseSuccess() {
        String originalUrl = "https://example.com";
        String shortUrl = "exmpl.com";
        ShortenedUrlDTO data = new ShortenedUrlDTO(originalUrl, shortUrl, 0);

        createShortenedUrlEntity(data);

        Optional<ShortenedUrlEntity> foundEntity = repository.findByOriginalUrl(originalUrl);

        assertThat(foundEntity.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not find a ShortenedUrlEntity in DB because original URL does not exist")
    public void findByOriginalUrlCaseNotFound() {
        String originalUrl = "https://example.com";

        Optional<ShortenedUrlEntity> foundEntity = repository.findByOriginalUrl(originalUrl);

        assertThat(foundEntity.isEmpty()).isTrue();
    }

    private ShortenedUrlEntity createShortenedUrlEntity(ShortenedUrlDTO dto) {
        ShortenedUrlEntity entity = new ShortenedUrlEntity(dto);
        entity.setId("a1");
        entityManager.persist(entity);
        return entity;
    }
}
