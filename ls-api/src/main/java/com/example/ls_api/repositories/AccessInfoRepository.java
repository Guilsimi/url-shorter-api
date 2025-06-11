package com.example.ls_api.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ls_api.model.entities.AccessInfoEntity;

public interface AccessInfoRepository extends MongoRepository<AccessInfoEntity, String> {

    public Optional<AccessInfoEntity> findByIp(String ip);

    public boolean existsByIp(String ip);

}
