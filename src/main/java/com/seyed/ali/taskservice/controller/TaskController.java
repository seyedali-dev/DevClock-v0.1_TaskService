package com.seyed.ali.taskservice.controller;

import com.seyed.ali.taskservice.model.domain.Task;
import com.seyed.ali.taskservice.model.payload.Result;
import com.seyed.ali.taskservice.model.payload.TaskDTO;
import com.seyed.ali.taskservice.service.interfaces.TaskService;
import com.seyed.ali.taskservice.util.converter.TaskConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/task")
@SecurityRequirement(name = "Keycloak")
@Tag(name = "Task Operation", description = "API for operation on task.")
public class TaskController {

    private final TaskService taskService;
    private final TaskConverter taskConverter;

    @PostMapping
    @Operation(summary = "Create a task", description = "Adds a new task to the database")
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

    @GetMapping("/{projectId}")
    @Operation(summary = "Get all task for project", description = "Fetches all tasks for associated project from the database", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskDTO.class)))
            )
    })
    public ResponseEntity<Result> findAllTasksForProject(@PathVariable String projectId) {
        List<Task> taskList = this.taskService.findAllTasksForProject(projectId);
        List<TaskDTO> response = this.taskConverter.convertTaskDTOList(taskList);

        return ResponseEntity.ok(new Result(
                true,
                OK,
                "List of all the tasks for project: '" + projectId + "'",
                response
        ));
    }

}
