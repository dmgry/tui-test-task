package com.ciklum.test.github.consumer.controller;

import com.ciklum.test.github.consumer.dto.BranchDto;
import com.ciklum.test.github.consumer.dto.CommitDto;
import com.ciklum.test.github.consumer.dto.OwnerDto;
import com.ciklum.test.github.consumer.dto.RepositoryDto;
import com.ciklum.test.github.consumer.properties.GithubProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public class GithubRepositoryControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private RestTemplate githubRestTemplate;
    @Autowired
    private GithubProperties properties;

    private MockMvc mockMvc;

    private MockRestServiceServer mockRestServiceServer;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        mockRestServiceServer = MockRestServiceServer.createServer(githubRestTemplate);
    }

    @Test
    public void shouldReturnSuccessRepositoriesByUsername() throws Exception {
        String username = "David";
        String branchName = "akka";

        initMockServerResponse(username, branchName);

        mockMvc.perform(get("/api/github/users/{username}/repositories", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("akka"))
                .andExpect(jsonPath("$[0].login").value("David"))
                .andExpect(jsonPath("$[0].branches", hasSize(2)))
                .andExpect(jsonPath("$[0].branches[0].name").value("Master"))
                .andExpect(jsonPath("$[0].branches[1].name").value("Dev"))
                .andExpect(jsonPath("$[0].branches[0].lastCommitSha").value("ascklefgj3409ru89dasuc98sdoivjowi"));
        mockRestServiceServer.verify();
    }

    @Test
    public void shouldReturnNotFoundByUsername() throws Exception {
        String notFoundUserName = "NotFoundUserName";
        String notFoundUserMsg = "{\n" +
                "\"message\": \"Not Found\",\n" +
                "\"documentation_url\": \"https://docs.github.com/rest/reference/repos#list-repositories-for-a-user\"\n" +
                "}";

        initMockServerNegativeResponse(notFoundUserName, notFoundUserMsg);

        mockMvc.perform(get("/api/github/users/{username}/repositories", notFoundUserName))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.message").value("Not found github user data, please enter valid username"));

        mockRestServiceServer.verify();
    }

    @Test
    public void shouldReturnErrorWhenHeaderAcceptNotApplicationJSON() {
        // TODO: implement
    }

    @Test
    public void shouldReturnErrorWhenGithubNotAvailable() {
        // TODO: implement
    }

    private void initMockServerNegativeResponse(String notFoundUserName, String notFoundUserMsg) {
        mockRestServiceServer.expect(requestTo(properties.getFullReposUrl(notFoundUserName)))
                .andRespond(withStatus(HttpStatus.NOT_FOUND)
                        .body(notFoundUserMsg)
                        .contentType(MediaType.APPLICATION_JSON));
    }

    private void initMockServerResponse(String username, String branchName) throws JsonProcessingException {
        String mockRepos = new ObjectMapper().writeValueAsString(mockReposDataFromGithub(username));
        mockRestServiceServer.expect(requestTo(properties.getFullReposUrl(username)))
                .andRespond(withSuccess(mockRepos, MediaType.APPLICATION_JSON));

        String mockBranches = new ObjectMapper().writeValueAsString(mockBranchesDataFromGithub());
        mockRestServiceServer.expect(requestTo(properties.getFullBranchesUrl(username, branchName)))
                .andRespond(withSuccess(mockBranches, MediaType.APPLICATION_JSON));
    }


    private List<RepositoryDto> mockReposDataFromGithub(String username) {
        return Arrays.asList(
                new RepositoryDto("akka", new OwnerDto(username), false),
                new RepositoryDto("akkaCustom", new OwnerDto(username), true)
        );
    }

    private List<BranchDto> mockBranchesDataFromGithub() {
        return Arrays.asList(
                new BranchDto("Master", new CommitDto("ascklefgj3409ru89dasuc98sdoivjowi")),
                new BranchDto("Dev", new CommitDto("ascklefgj34090000dasuc98sdoivjowi"))
        );
    }
}
