package com.example.ls_api.model.entities;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccessRegister {

    private Instant accessDate;
    private String shortLinkId;

}
