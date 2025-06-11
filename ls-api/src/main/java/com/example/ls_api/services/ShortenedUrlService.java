package com.example.ls_api.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ls_api.model.entities.ShortenedUrlEntity;
import com.example.ls_api.repositories.ShortenedUrlRepository;
import com.example.ls_api.services.exceptions.UrlShortenerException;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ShortenedUrlService {

    @Autowired
    private ShortenedUrlRepository repository;

    public ShortenedUrlEntity findByOriginalUrl(String url) {
        return repository.findByOriginalUrl(url).orElse(null);
    }

    private ShortenedUrlEntity findById(String id) {
        return repository.findById(id).orElseThrow(
                () -> new RuntimeException("Usuário não encontrado"));
    }

    public ShortenedUrlEntity findById(String id, String possibleErrorMessage,
            String url) {
        return repository.findById(id).orElseThrow(
                () -> new UrlShortenerException(possibleErrorMessage, url));
    }

    public List<ShortenedUrlEntity> findAll() {
        return repository.findAll();
    }

    public ShortenedUrlEntity createShortenedUrl(String url, HttpServletRequest req) {
        String id = generateId();

        ShortenedUrlEntity entity = new ShortenedUrlEntity(
                id,
                url,
                getHostNameAndOptionalPort(req) + id,
                0);

        return repository.save(entity);
    }

    private String generateId() {
        String uuid;

        do {
            uuid = "sl-" + UUID.randomUUID().toString().substring(0, 4);
        } while (repository.existsById(uuid));

        return uuid;
    }

    private String getHostNameAndOptionalPort(HttpServletRequest req) {
        int serverPort = req.getServerPort();
        String serverHost = req.getServerName();
        String serverScheme = req.getScheme();

        if (serverPort == 80 || serverPort == 443 || serverPort == -1) {
            return serverScheme + "://" + serverHost + "/";
        } else {
            return serverScheme + "://" + serverHost + ":" + serverPort + "/";
        }
    }

    public void updateClicksNumber(ShortenedUrlEntity entity) {
        entity.setClicks(entity.getClicks() + 1);
        update(entity);
    }

    private void update(ShortenedUrlEntity newEntity) {
        ShortenedUrlEntity oldEntity = findById(newEntity.getId());
        updateData(oldEntity, newEntity);
        repository.save(newEntity);
    }

    private ShortenedUrlEntity updateData(ShortenedUrlEntity oldEntity, ShortenedUrlEntity newEntity) {
        return new ShortenedUrlEntity(
                newEntity.getId() != null ? newEntity.getId() : oldEntity.getId(),
                newEntity.getOriginalUrl() != null ? newEntity.getOriginalUrl() : oldEntity.getOriginalUrl(),
                newEntity.getShortUrl() != null ? newEntity.getShortUrl() : oldEntity.getShortUrl(),
                newEntity.getClicks() != null ? newEntity.getClicks() : oldEntity.getClicks());
    }

}
