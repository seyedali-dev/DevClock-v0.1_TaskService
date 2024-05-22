package com.seyed.ali.TaskService.controller;

import com.seyed.ali.TaskService.model.domain.Task;
import com.seyed.ali.TaskService.model.payload.Result;
import com.seyed.ali.TaskService.model.payload.TaskDTO;
import com.seyed.ali.TaskService.service.interfaces.TaskService;
import com.seyed.ali.TaskService.util.converter.TaskConverter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/task")
@SecurityRequirement(name = "Keycloak")
public class TaskController {

    private final TaskService taskService;
    private final TaskConverter taskConverter;

    @PostMapping
    public ResponseEntity<Result> createTask(@RequestBody TaskDTO taskDTO) {
        Task task = this.taskConverter.convertToTask(taskDTO);
        Task createdTask = this.taskService.createTask(task);
        TaskDTO response = this.taskConverter.convertToTaskDTO(createdTask);

        return ResponseEntity.status(CREATED).body(new Result(
                true,
                CREATED,
                "Task created successfully.",
                response
        ));
    }

}
