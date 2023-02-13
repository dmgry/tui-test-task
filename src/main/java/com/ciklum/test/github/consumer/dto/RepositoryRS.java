package com.ciklum.test.github.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RepositoryRS {

    private String name;
    private String login;
    private List<BranchRS> branches;
}
