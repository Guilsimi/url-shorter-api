package com.example.ls_api.model.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.ls_api.model.dto.ShortenedUrlDTO;
import com.example.ls_api.model.dto.UrlShortenResponse;
import com.example.ls_api.model.entities.ShortenedUrlEntity;

@Mapper(componentModel = "spring")
public interface ShortenUrlMapper {

    public UrlShortenResponse shortenedUrlToResponseDto(ShortenedUrlEntity entity);

    public List<ShortenedUrlDTO> shortenedUrlToDTO(List<ShortenedUrlEntity> entity);

}
