package com.ciklum.test.github.consumer.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "github.api")
public class GithubProperties {

    private String baseurl;
    private String reposUrl;
    private String branchesUrl;

    public String getFullReposUrl(String username) {
        return String.format(baseurl + reposUrl, username);
    }

    public String getFullBranchesUrl(String username, String branchName) {
        return String.format(baseurl + branchesUrl, username, branchName);
    }
}