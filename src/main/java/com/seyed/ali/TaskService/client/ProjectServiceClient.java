package com.seyed.ali.TaskService.client;

import com.seyed.ali.TaskService.exceptions.ResourceNotFoundException;
import com.seyed.ali.TaskService.model.payload.Result;
import com.seyed.ali.TaskService.util.KeycloakSecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@SuppressWarnings("FieldCanBeLocal")
@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectServiceClient {

    private final String projectServiceBaseURL = "http://localhost:8083/api/v1"; // TODO: remember to change the host and port when dockerizing the application

    private final RestClient restClient;
    private final KeycloakSecurityUtil keycloakSecurityUtil;

    public boolean validateProjectsExistence(String projectId) {
        String jwt = this.keycloakSecurityUtil.extractTokenFromSecurityContext();

        Result result = this.restClient
                .get().uri(this.projectServiceBaseURL + "/project/client/" + projectId)
                .header("Authorization", "Bearer " + jwt)
                .retrieve()
                .onStatus(NOT_FOUND::equals,
                        (request, response) -> {
                            throw new ResourceNotFoundException("Project with ProjectID '" + projectId + "' not found");
                        })
                .body(Result.class);

        assert result != null;
        return (boolean) result.getData();
    }

}
