package com.seyed.ali.TaskService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seyed.ali.TaskService.config.EurekaClientTestConfiguration;
import com.seyed.ali.TaskService.exceptions.ResourceNotFoundException;
import com.seyed.ali.TaskService.model.domain.Task;
import com.seyed.ali.TaskService.model.enums.TaskStatus;
import com.seyed.ali.TaskService.model.payload.TaskDTO;
import com.seyed.ali.TaskService.service.interfaces.TaskService;
import com.seyed.ali.TaskService.util.KeycloakSecurityUtil;
import com.seyed.ali.TaskService.util.converter.TaskConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("unused")
@WebMvcTest(TaskController.class) /* since this is not in integration test, rather controller unit test */
@EnableConfigurationProperties /* to use application.yml-test file */
@ActiveProfiles("test")
@AutoConfigureMockMvc/* calling the api itself */
@ContextConfiguration(classes = {EurekaClientTestConfiguration.class}) /* to call the configuration in the test (for service-registry configs) */
class TaskControllerTest {

    private @MockBean TaskService taskService;
    private @MockBean TaskConverter taskConverter;
    private @MockBean KeycloakSecurityUtil keycloakSecurityUtil;
    private @Autowired MockMvc mockMvc;
    private @Autowired ObjectMapper objectMapper;

    @Test
    public void testCreateTask() throws Exception {
        // given
        TaskDTO taskDTO = new TaskDTO("1", "Test Task", "Description", TaskStatus.IN_PROGRESS, 1, "123");
        Task task = new Task();
        task.setTaskId("1");
        task.setTaskName("Test Task");
        task.setTaskDescription("Description");
        task.setTaskStatus(TaskStatus.IN_PROGRESS);
        task.setProjectId("123");
        task.setTaskOrder(1);

        when(this.taskConverter.convertToTask(taskDTO)).thenReturn(task);
        when(this.taskService.createTask(task)).thenReturn(task);
        when(this.taskConverter.convertToTaskDTO(task)).thenReturn(taskDTO);

        // when
        ResultActions resultActions = this.mockMvc.perform(
                post("/api/v1/task")
                        .with(jwt().authorities(new SimpleGrantedAuthority("some_authority")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(taskDTO))
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.httpStatus").value("CREATED"))
                .andExpect(jsonPath("$.message").value("Task created successfully."))
                .andExpect(jsonPath("$.data.taskId").value("1"))
                .andExpect(jsonPath("$.data.taskName").value("Test Task"))
                .andExpect(jsonPath("$.data.taskDescription").value("Description"))
                .andExpect(jsonPath("$.data.taskStatus").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.data.projectId").value("123"))
                .andExpect(jsonPath("$.data.taskOrder").value(1));
    }

    @Test
    public void testFindAllTasksForProject() throws Exception {
        // given
        String projectId = "123";
        List<Task> tasks = List.of(
                new Task("1", "Task 1", "Description 1", TaskStatus.IN_PROGRESS, 1, projectId),
                new Task("2", "Task 2", "Description 2", TaskStatus.PLANNED_FOR_FUTURE, 2, projectId)
        );
        List<TaskDTO> taskDTOs = List.of(
                new TaskDTO("1", "Task 1", "Description 1", TaskStatus.IN_PROGRESS, 1, projectId),
                new TaskDTO("2", "Task 2", "Description 2", TaskStatus.PLANNED_FOR_FUTURE, 2, projectId)
        );

        when(this.taskService.findAllTasksForProject(projectId)).thenReturn(tasks);
        when(this.taskConverter.convertTaskDTOList(tasks)).thenReturn(taskDTOs);

        // when
        ResultActions resultActions = this.mockMvc.perform(
                get("/api/v1/task/{projectId}", projectId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("some_authority")))
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("List of all the tasks for project: '" + projectId + "'"))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].taskId").value("1"))
                .andExpect(jsonPath("$.data[0].taskName").value("Task 1"))
                .andExpect(jsonPath("$.data[0].taskDescription").value("Description 1"))
                .andExpect(jsonPath("$.data[0].taskStatus").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.data[0].projectId").value(projectId))
                .andExpect(jsonPath("$.data[0].taskOrder").value(1))
                .andExpect(jsonPath("$.data[1].taskId").value("2"))
                .andExpect(jsonPath("$.data[1].taskName").value("Task 2"))
                .andExpect(jsonPath("$.data[1].taskDescription").value("Description 2"))
                .andExpect(jsonPath("$.data[1].taskStatus").value("PLANNED_FOR_FUTURE"))
                .andExpect(jsonPath("$.data[1].projectId").value(projectId))
                .andExpect(jsonPath("$.data[1].taskOrder").value(2));
    }

    @Test
    public void testFindAllTasksForProject_NonExistingProjectId() throws Exception {
        // given
        String projectId = "non_existing_project_id";
        when(this.taskService.findAllTasksForProject(projectId)).thenThrow(new ResourceNotFoundException("some message"));

        // when
        ResultActions resultActions = this.mockMvc.perform(
                get("/api/v1/task/{projectId}", projectId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("some_authority")))
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("The requested resource was not found."))
                .andExpect(jsonPath("$.data", is("ServerMessage - some message")));
    }

    @Test
    public void testFindAllTasksForProject_EmptyTaskList() throws Exception {
        // given
        String projectId = "123";
        when(taskService.findAllTasksForProject(projectId)).thenReturn(Collections.emptyList());

        // when
        ResultActions resultActions = this.mockMvc.perform(
                get("/api/v1/task/{projectId}", projectId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("some_authority")))
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("List of all the tasks for project: '123'"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    // Utility method to convert object to JSON string
    private String asJsonString(final Object obj) {
        try {
            return this.objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
