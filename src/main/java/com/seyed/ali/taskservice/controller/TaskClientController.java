package com.seyed.ali.taskservice.controller;

import com.seyed.ali.taskservice.model.payload.ProjectDTO;
import com.seyed.ali.taskservice.model.payload.Result;
import com.seyed.ali.taskservice.model.payload.TaskDTO;
import com.seyed.ali.taskservice.service.interfaces.TaskClientService;
import com.seyed.ali.taskservice.util.converter.TaskConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

/**
 * This controller is used for microservice intercommunication.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/task/client")
@SecurityRequirement(name = "Keycloak")
@Tag(name = "Task Client", description = "API for intercommunication of microservices.")
public class TaskClientController {

    private final TaskClientService taskClientService;
    private final TaskConverter taskConverter;

    @GetMapping("/{taskId}")
    @Operation(summary = "Validate Task Existence", description = "Validates whether the specified `taskID` is valid or not.", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = Result.class))
            )
    })
    public Result validateTasksExistence(@PathVariable String taskId) {
        return new Result(
                true,
                OK,
                "The task with id " + taskId + " exists and is valid.",
                this.taskClientService.validateTasksExistence(taskId)
        );
    }

    @GetMapping
    @Operation(summary = "Get Task By its Name", description = "Fetches the task based on the TaskName", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = TaskDTO.class))
            )
    })
    public TaskDTO getTaskByNameOrId(@RequestParam("identifier") String identifier) {
        return this.taskConverter.convertToTaskDTO(this.taskClientService.getTaskByName(identifier));
    }

}
