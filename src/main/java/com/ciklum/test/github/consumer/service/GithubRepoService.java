package com.ciklum.test.github.consumer.service;

import com.ciklum.test.github.consumer.dto.RepositoryResponse;

import java.util.List;

public interface GithubRepoService {

    List<RepositoryResponse> getUserRepositories(String username);
}
