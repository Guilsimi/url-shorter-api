package com.example.ls_api.model.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "access_info")
public class AccessInfoEntity {

    @Id
    @EqualsAndHashCode.Include
    private String ip;

    private String userAgent;

    @Setter(AccessLevel.NONE)
    private List<AccessRegister> accessRegisters;

}
