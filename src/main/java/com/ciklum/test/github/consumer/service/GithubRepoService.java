package com.ciklum.test.github.consumer.service;

import com.ciklum.test.github.consumer.dto.RepositoryRS;

import java.util.List;

public interface GithubRepoService {

    List<RepositoryRS> getUserRepositories(String username);
}
