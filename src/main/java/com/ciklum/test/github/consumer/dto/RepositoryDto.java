package com.ciklum.test.github.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RepositoryDto {

    private String name;
    private OwnerDto owner;
    private boolean fork;
}
