package com.seyed.ali.taskservice.controller;

import com.seyed.ali.taskservice.model.domain.Task;
import com.seyed.ali.taskservice.repository.TaskRepository;
import com.seyed.ali.taskservice.util.KeycloakSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("test")
@SecurityRequirement(name = "Keycloak")
@Tag(name = "Test", description = "API for testing.")
public class TestController {

    private final KeycloakSecurityUtil keycloakSecurityUtil;
    private final TaskRepository taskRepository;

    @PostMapping
    @Operation(summary = "Make a rest call to keycloak", description = "Makes a REST call to keycloak to obtain an access_token for authentication between clients (microservices).")
    public void test() {
        System.out.println("--> accesstoken: " + this.keycloakSecurityUtil.getAccessToken());
    }

    @GetMapping
    @Operation(summary = "Find all tasks in db")
    public List<Task> getAllTasks() {
        return this.taskRepository.findAll();
    }

}
