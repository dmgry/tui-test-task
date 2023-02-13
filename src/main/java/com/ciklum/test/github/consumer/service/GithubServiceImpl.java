package com.ciklum.test.github.consumer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ciklum.test.github.consumer.dto.BranchDto;
import com.ciklum.test.github.consumer.dto.BranchResponse;
import com.ciklum.test.github.consumer.dto.RepositoryDto;
import com.ciklum.test.github.consumer.dto.RepositoryResponse;
import com.ciklum.test.github.consumer.properties.GithubProperties;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class GithubServiceImpl implements GithubRepoService {

    public static final String EMPTY_STRING = "";

    private GithubProperties props;
    private RestTemplate githubRestTemplate;

    @Override
    public List<RepositoryResponse> getUserRepositories(String username) {
        log.info("Fetching repositories data for user: {}", username);
        String repoPathWithName = String.format(props.getReposUrl(), username);
        ResponseEntity<List<RepositoryDto>> repositories = githubRestTemplate.exchange(repoPathWithName,
                                                                                       HttpMethod.GET, null,
                                                                                       new ParameterizedTypeReference<>() {
                                                                                       });

        return repositories.getBody()
            .stream().parallel()
            .filter(repo -> !repo.isFork())
            .map(repo -> getRepositoryResponse(username, repo))
            .collect(Collectors.toList());
    }

    private RepositoryResponse getRepositoryResponse(String username, RepositoryDto repo) {
        String branchPath = String.format(props.getBranchesUrl(), username, repo.getName());
        // TODO: potential point of problem, it depends on business, should we log it or interrupt or other action
        ResponseEntity<List<BranchDto>> branchDtos = githubRestTemplate.exchange(branchPath, HttpMethod.GET, null,
                                                                                 new ParameterizedTypeReference<>() {
                                                                                 });

        return mapToRepoRS(repo, branchDtos.getBody());
    }

    private RepositoryResponse mapToRepoRS(RepositoryDto repository, List<BranchDto> branchDtos) {
        List<BranchResponse> branchesRS = branchDtos.stream()
            .map(br -> new BranchResponse(br.getName(), getSha(br)))
            .collect(Collectors.toList());

        return new RepositoryResponse(repository.getName(), repository.getOwner().getLogin(), branchesRS);
    }

    private String getSha(BranchDto br) {
        return br != null ? br.getCommit().getSha() : EMPTY_STRING;

    }
}
