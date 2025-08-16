package com.example.ls_api.model.entities;

import com.example.ls_api.model.dto.ShortenedUrlDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "shortened_urls")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ShortenedUrlEntity {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @Column(unique = true)
    private String originalUrl;

    @Column(unique = true)
    private String shortUrl;

    private Integer clicks;

    public ShortenedUrlEntity(ShortenedUrlDTO dto) {
        this.originalUrl = dto.originalUrl();
        this.shortUrl = dto.shortUrl();
        this.clicks = dto.clicks();
    }

}
