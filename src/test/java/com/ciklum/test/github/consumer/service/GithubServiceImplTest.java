package com.ciklum.test.github.consumer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.ciklum.test.github.consumer.dto.BranchDto;
import com.ciklum.test.github.consumer.dto.CommitDto;
import com.ciklum.test.github.consumer.dto.OwnerDto;
import com.ciklum.test.github.consumer.dto.RepositoryDto;
import com.ciklum.test.github.consumer.dto.RepositoryResponse;
import com.ciklum.test.github.consumer.properties.GithubProperties;

@ExtendWith(MockitoExtension.class)
public class GithubServiceImplTest {

    @Mock
    private RestTemplate githubRestTemplate;

    @Mock
    private GithubProperties props;

    @InjectMocks
    private GithubServiceImpl githubService;

    @Test
    public void shouldReturnSuccessGetRepositories() {
        List<RepositoryDto> expectedResult = new ArrayList<>();
        expectedResult.add(new RepositoryDto("Akka", new OwnerDto("testUsername"), false));

        mockMethodInvocation(expectedResult);

        List<RepositoryResponse> actualResult = githubService.getUserRepositories("testUsername");

        assertEquals(expectedResult.get(0).getName(), actualResult.get(0).getName());
        assertEquals(expectedResult.get(0).getOwner().getLogin(), actualResult.get(0).getLogin());
        assertEquals("Master", actualResult.get(0).getBranches().get(0).getName());
        assertEquals("DEV", actualResult.get(0).getBranches().get(1).getName());
        assertEquals("QA", actualResult.get(0).getBranches().get(2).getName());
        assertEquals("1235orjg9084tu428349ru2389ru289023ur",
                     actualResult.get(0).getBranches().get(2).getLastCommitSha());
    }

    @Test
    public void shouldReturnSuccessRepoWhichForkIsFalse() {
        List<RepositoryDto> expectedResult = new ArrayList<>();
        expectedResult.add(new RepositoryDto("Akka", new OwnerDto("testUsername"), false));
        expectedResult.add(new RepositoryDto("Akka_CUstom", new OwnerDto("testUsername"), true));

        mockMethodInvocation(expectedResult);
        List<RepositoryResponse> actualResult = githubService.getUserRepositories("testUsername");

        assertEquals(1, actualResult.size());
        assertEquals("Akka", actualResult.get(0).getName());
        assertEquals(3, actualResult.get(0).getBranches().size());
    }

    private void mockMethodInvocation(List<RepositoryDto> expectedResult) {
        String repoUrl = "/users/testUsername/repos?type=owner";
        String branchUrl = "/repos/testUsername/Akka/branches";
        when(props.getReposUrl()).thenReturn(repoUrl);
        when(props.getBranchesUrl()).thenReturn(branchUrl);

        when(githubRestTemplate.exchange(eq(repoUrl),
                                         eq(HttpMethod.GET),
                                         eq(null),
                                         any(ParameterizedTypeReference.class)))
            .thenReturn(new ResponseEntity<>(expectedResult, HttpStatus.OK));

        List<BranchDto> branchMock = createBranchMock();
        when(githubRestTemplate.exchange(eq(branchUrl),
                                         eq(HttpMethod.GET),
                                         eq(null),
                                         any(ParameterizedTypeReference.class)))
            .thenReturn(ResponseEntity.ok(branchMock));
    }

    private List<BranchDto> createBranchMock() {
        List<BranchDto> mockBranchDto = new ArrayList<>();
        mockBranchDto.add(new BranchDto("Master", new CommitDto("1233orjg9084tu428349ru2389ru289023ur")));
        mockBranchDto.add(new BranchDto("DEV", new CommitDto("1234orjg9084tu428349ru2389ru289023ur")));
        mockBranchDto.add(new BranchDto("QA", new CommitDto("1235orjg9084tu428349ru2389ru289023ur")));
        return mockBranchDto;
    }
}
