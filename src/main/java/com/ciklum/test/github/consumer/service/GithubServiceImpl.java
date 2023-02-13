package com.ciklum.test.github.consumer.service;

import com.ciklum.test.github.consumer.dto.BranchDto;
import com.ciklum.test.github.consumer.dto.BranchRS;
import com.ciklum.test.github.consumer.dto.RepositoryDto;
import com.ciklum.test.github.consumer.dto.RepositoryRS;
import com.ciklum.test.github.consumer.properties.GithubProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class GithubServiceImpl implements GithubRepoService {

    private GithubProperties props;
    private RestTemplate restTemplate;

    @Override
    public List<RepositoryRS> getUserRepositories(String username) {
        log.info("Fetching repositories data for user: {}", username);
        String repoPathWithName = String.format(props.getReposUrl(), username);
        RepositoryDto[] repositories = restTemplate.getForObject(repoPathWithName, RepositoryDto[].class);

        return Arrays.asList(repositories)
                .stream().parallel()
                .filter(repo -> !repo.isFork())
                .map(repo -> {
                    String branchPath = String.format(props.getBranchesUrl(), username, repo.getName());
                    // TODO: potential point of problem, it depends on business, should we log it or interrupt or other action
                    BranchDto[] branchDtos = restTemplate.getForObject(branchPath, BranchDto[].class);
                    return mapToRepoRS(repo, branchDtos);
                }).collect(Collectors.toList());
    }

    private RepositoryRS mapToRepoRS(RepositoryDto repository, BranchDto[] branchDtos) {
        List<BranchRS> branchesRS = Arrays.asList(branchDtos)
                .stream()
                .map(br -> new BranchRS(br.getName(), getSha(br)))
                .collect(Collectors.toList());

        return new RepositoryRS(repository.getName(), repository.getOwner().getLogin(), branchesRS);
    }

    private String getSha(BranchDto br) {
        return br != null ? br.getCommit().getSha() : "";

    }
}
