package com.ciklum.test.github.consumer.controller;


import com.ciklum.test.github.consumer.dto.RepositoryRS;
import com.ciklum.test.github.consumer.service.GithubRepoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/github")
@AllArgsConstructor
public class GithubRepoController {

    private GithubRepoService repoService;

    @GetMapping("/{username}/repositories")
    public ResponseEntity<List<RepositoryRS>> getRepoByName(@PathVariable String username) {
        log.info("Received incoming request fetch repo data for username: {} ",username);
        List<RepositoryRS> repoRs = repoService.getUserRepositories(username);

        return ResponseEntity.ok(repoRs);
    }
}
