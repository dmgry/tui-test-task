package com.ciklum.test.github.consumer.controller;


import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ciklum.test.github.consumer.dto.RepositoryResponse;
import com.ciklum.test.github.consumer.service.GithubRepoService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/github")
@AllArgsConstructor
public class GithubRepoController {

    private GithubRepoService repoService;

    @ApiImplicitParams({
        @ApiImplicitParam(name = "accept", value = "application/json", required = true, allowEmptyValue = false,
            paramType = "header", dataTypeClass = String.class, example = "application/json"),
        @ApiImplicitParam(name = "content-type", value = "application/json", required = true, allowEmptyValue = false,
            paramType = "header", dataTypeClass = String.class, example = "application/json")
    })
    @GetMapping(value = "users/{username}/repositories",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RepositoryResponse>> getRepoByName(@PathVariable String username) {
        log.info("Received incoming request fetch repo data for username: {} ", username);
        List<RepositoryResponse> repoRs = repoService.getUserRepositories(username);

        return ResponseEntity.ok(repoRs);
    }
}
