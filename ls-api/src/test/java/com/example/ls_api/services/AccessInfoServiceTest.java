package com.example.ls_api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.ls_api.model.entities.AccessInfoEntity;
import com.example.ls_api.model.entities.AccessRegister;
import com.example.ls_api.repositories.AccessInfoRepository;

import jakarta.servlet.http.HttpServletRequest;

public class AccessInfoServiceTest {

    @Mock
    private AccessInfoRepository repository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    AccessInfoService service;

    @Captor
    ArgumentCaptor<AccessInfoEntity> argumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should actualize an existing AccessRegister to actual instant")
    void updateAccessDateCaseAccessRegisterExists() {
        Instant firstInstant = Instant.now();
        AccessInfoEntity entity = new AccessInfoEntity(
                "123.456.789.000", "Test User Agent",
                Arrays.asList(new AccessRegister(firstInstant, "exmple.com")));

        when(repository.existsByIp(any())).thenReturn(true);
        when(request.getRemoteAddr()).thenReturn("123.456.789.000");
        when(request.getHeader("User-Agent")).thenReturn("Test User Agent");
        when(request.getRequestURL()).thenReturn(new StringBuffer("exmple.com"));

        when(repository.findByIp(anyString())).thenReturn(Optional.of(entity));

        service.insertAccessInfoData(request);

        verify(repository, times(1)).existsByIp(any());
        verify(repository, times(1)).findByIp(anyString());
        verify(repository, times(1)).save(entity);

        assertTrue(firstInstant.isBefore(entity.getAccessRegisters().get(0).getAccessDate()));
    }

    @Test
    @DisplayName("Should actualize an existing AccessRegister to actual instant")
    void addNewAccessRegisterInExistingAccessInfo() {
        AccessInfoEntity entity = new AccessInfoEntity(
                "123.456.789.000", "Test User Agent",
                new ArrayList<>());

        when(repository.existsByIp(any())).thenReturn(true);
        when(request.getRemoteAddr()).thenReturn("123.456.789.000");
        when(request.getHeader("User-Agent")).thenReturn("Test User Agent");
        when(request.getRequestURL()).thenReturn(new StringBuffer("exmple.com"));

        when(repository.findByIp(anyString())).thenReturn(Optional.of(entity));

        assertTrue(entity.getAccessRegisters().isEmpty());

        service.insertAccessInfoData(request);

        verify(repository, times(1)).existsByIp(any());
        verify(repository, times(1)).findByIp(anyString());
        verify(repository, times(1)).save(entity);

        assertEquals(entity.getAccessRegisters().size(), 1);
        assertEquals(entity.getAccessRegisters().get(0).getShortLink(), request.getRequestURL().toString());
    }

    @Test
    @DisplayName("Should create new access info entity in DB successfully")
    void testCreateAccessInfoData() {
        when(repository.existsByIp(any())).thenReturn(false);
        when(request.getRemoteAddr()).thenReturn("123.456.789.000");
        when(request.getHeader("User-Agent")).thenReturn("Test User Agent");
        when(request.getRequestURL()).thenReturn(new StringBuffer("exmple.com"));

        service.insertAccessInfoData(request);

        verify(repository, times(1)).save(argumentCaptor.capture());

        AccessInfoEntity entity = argumentCaptor.getValue();

        assertEquals(entity.getIp(), request.getRemoteAddr());
        assertEquals(entity.getUserAgent(), request.getHeader("User-Agent"));
        assertEquals(entity.getAccessRegisters().size(), 1);
        assertEquals(entity.getAccessRegisters().get(0).getShortLink(), request.getRequestURL().toString());
    }

    @Test
    @DisplayName("Should throw an exception when does not exist an valid access info with the ip")
    void findByIpThrowException() throws RuntimeException {

        when(repository.existsByIp(any())).thenReturn(true);
        when(request.getRemoteAddr()).thenReturn("123.456.789.000");

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            service.insertAccessInfoData(request);
        });

        assertEquals(exception.getMessage(), "Access information not found");
    }

}
