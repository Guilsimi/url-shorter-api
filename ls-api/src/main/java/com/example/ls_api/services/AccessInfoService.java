package com.example.ls_api.services;

import java.time.Instant;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ls_api.model.entities.AccessInfoEntity;
import com.example.ls_api.model.entities.AccessRegister;
import com.example.ls_api.repositories.AccessInfoRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AccessInfoService {

    @Autowired
    private AccessInfoRepository repository;

    public void insertAccessInfoData(HttpServletRequest request) {
        if (!existsByIp(request.getRemoteAddr())) {
            createAccessInfoData(request);
        } else {
            addAccessRegister(request);
        }
    }

    private void addAccessRegister(HttpServletRequest request) {
        AccessInfoEntity entity = findByIp(request.getRemoteAddr());
        String requestUri = request.getRequestURL().toString();
        AccessRegister register = entity.getAccessRegisters().stream()
                .filter(reg -> reg.getShortLink().equals(requestUri)).findFirst().orElse(null);

        if (register == null) {
            entity.getAccessRegisters().add(new AccessRegister(Instant.now(),
                    requestUri));
            repository.save(entity);
        } else {
            register.setAccessDate(Instant.now());
            repository.save(entity);
        }
    }

    private void createAccessInfoData(HttpServletRequest request) {
        AccessRegister register = new AccessRegister(Instant.now(),
                request.getRequestURL().toString());

        AccessInfoEntity entity = new AccessInfoEntity(
                request.getRemoteAddr(),
                request.getHeader("User-Agent"),
                Arrays.asList(register));

        repository.save(entity);
    }

    private boolean existsByIp(String ip) {
        return repository.existsByIp(ip);
    }

    private AccessInfoEntity findByIp(String ip) {
        return repository.findByIp(ip).orElseThrow(
                () -> new RuntimeException("Access information not found"));
    }

}
