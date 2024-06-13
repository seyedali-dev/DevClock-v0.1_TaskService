package com.seyed.ali.taskservice.service;

import com.seyed.ali.taskservice.client.ProjectServiceClient;
import com.seyed.ali.taskservice.exceptions.OperationNotSupportedException;
import com.seyed.ali.taskservice.exceptions.ResourceNotFoundException;
import com.seyed.ali.taskservice.model.domain.Task;
import com.seyed.ali.taskservice.model.enums.TaskStatus;
import com.seyed.ali.taskservice.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    private @InjectMocks TaskServiceImpl taskService;
    private @Mock TaskRepository taskRepository;
    private @Mock ProjectServiceClient projectServiceClient;

    private Task task;

    @BeforeEach
    void setUp() {
        this.task = new Task();
        this.task.setTaskId("1");
        this.task.setTaskName("Test Task");
        this.task.setProjectId("123");
        this.task.setTaskOrder(1);
    }

    @Test
    @DisplayName("createTask should return success when taskOrderIsProvided")
    public void createTask_ProvidedTaskOrder_Success() {
        // Given
        when(this.projectServiceClient.validateProjectsExistence(anyString())).thenReturn(true);
        when(this.taskRepository.save(isA(Task.class))).thenReturn(this.task);

        // When
        Task result = this.taskService.createTask(this.task);
        System.out.println(result);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTaskOrder()).isEqualTo(1);

        verify(this.taskRepository, times(1)).existsByProjectIdAndTaskOrder(isA(String.class), isA(Integer.class));
        verify(this.taskRepository, times(1)).save(this.task);
    }

    @Test
    @DisplayName("createTask should return success when taskOrderIsNOTProvided")
    public void createTask_NOTProvidedTaskOrder_Success() {
        // Given
        Task task = new Task();
        task.setTaskId("1");
        task.setTaskName("Test Task");
        task.setProjectId("123");

        when(this.projectServiceClient.validateProjectsExistence(anyString())).thenReturn(true);
        when(this.taskRepository.findMaxTaskOrderByProjectId("123")).thenReturn(null); // Assuming no existing tasks
        when(this.taskRepository.save(task)).thenReturn(task);

        // When
        Task result = this.taskService.createTask(task);
        System.out.println(result);

        // Then
        assertThat(result.getTaskOrder().intValue()).isEqualTo(1);
        // Verify that the task was saved with order 1
        verify(this.taskRepository, times(1)).save(isA(Task.class));
    }

    @Test
    @DisplayName("createTask should return success when taskOrderIsNOTProvided & ThereAreTasksInDB")
    public void createTask_NOTProvidedTaskOrder_TasksExist_Success() {
        // Given
        Task task = new Task();
        task.setTaskId("1");
        task.setTaskName("Test Task");
        task.setProjectId("123");

        when(this.projectServiceClient.validateProjectsExistence(anyString())).thenReturn(true);
        when(this.taskRepository.findMaxTaskOrderByProjectId("123")).thenReturn(2); // Assuming existing tasks with max order 2
        when(this.taskRepository.save(isA(Task.class))).thenReturn(task);

        // When
        Task result = taskService.createTask(task);
        System.out.println(result);

        // Then
        assertThat(result.getTaskOrder().intValue()).isEqualTo(3);
        // Verify that the task was saved with order 3 (next sequential order)
        verify(this.taskRepository, times(1)).save(isA(Task.class));
    }

    @Test
    @DisplayName("createTask should return exception when taskOrderIsProvided & TaskOrderIsDuplicated")
    public void createTask_ProvidedTaskOrder_TaskOrderDuplicate_Exception() {
        // Given
        Task existingTask = new Task();
        existingTask.setTaskId("1");
        existingTask.setTaskName("Existing Task");
        existingTask.setProjectId("123");
        existingTask.setTaskOrder(1);

        Task newTask = new Task();
        newTask.setTaskId("2");
        newTask.setTaskName("New Task");
        newTask.setProjectId("123");
        newTask.setTaskOrder(1); // Same order as existing task

        when(this.projectServiceClient.validateProjectsExistence(anyString())).thenReturn(true);
        when(this.taskRepository.existsByProjectIdAndTaskOrder("123", 1)).thenReturn(true);

        // When
        // Call the service method (should throw IllegalArgumentException)
        Throwable thrown = catchThrowable(() -> this.taskService.createTask(newTask));
        System.out.println(thrown.getMessage());

        // Then
        assertThat(thrown).isInstanceOf(OperationNotSupportedException.class);
        assertThat(thrown.getMessage()).isEqualTo("Task order 1 already exists for the project.");
    }

    @Test
    @DisplayName("findAllTasksForProject should return success when ProjectIDIsValid & TasksExistInDB")
    public void findAllTasksForProject_ValidProjectIdAndTasksExist_Success() {
        // Given
        String projectId = "123";
        List<Task> tasks = List.of(
                new Task("1", "Task 1", "Description 1", TaskStatus.IN_PROGRESS, 1, projectId),
                new Task("2", "Task 2", "Description 2", TaskStatus.PLANNED_FOR_FUTURE, 2, projectId)
        );

        when(this.projectServiceClient.validateProjectsExistence(isA(String.class))).thenReturn(true);
        when(this.taskRepository.findByProjectIdOrderByTaskOrderAsc(isA(String.class))).thenReturn(tasks);

        // When
        List<Task> result = this.taskService.findAllTasksForProject(projectId);
        System.out.println(result);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(tasks);
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("findAllTasksForProject should return success when ProjectIDIsNOTValid")
    public void findAllTasksForProject_NOTValidProjectId_Exception() {
        // Given
        String projectId = "456";
        String expectedExceptionMessage = "Project with ProjectID '" + projectId + "' not found";
        when(this.projectServiceClient.validateProjectsExistence(projectId)).thenThrow(new ResourceNotFoundException(expectedExceptionMessage));

        // When
        Throwable thrown = catchThrowable(() -> this.taskService.findAllTasksForProject(projectId));
        System.out.println(thrown.getMessage());

        // Then
        assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
        assertThat(thrown.getMessage()).isEqualTo(expectedExceptionMessage);
    }

}