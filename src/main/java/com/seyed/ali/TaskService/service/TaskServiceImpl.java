package com.seyed.ali.TaskService.service;

import com.seyed.ali.TaskService.client.ProjectServiceClient;
import com.seyed.ali.TaskService.model.domain.Task;
import com.seyed.ali.TaskService.repository.TaskRepository;
import com.seyed.ali.TaskService.service.interfaces.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectServiceClient projectServiceClient;

    @Override
    public Task createTask(Task task) {
        task.setTaskId(UUID.randomUUID().toString());
        if (this.projectServiceClient.validateProjectsExistence(task.getProjectId()))
            task.setProjectId(task.getProjectId());
        return this.taskRepository.save(task);
    }

    @Override
    public List<Task> findAllTasksForProject(String projectId) {
        this.projectServiceClient.validateProjectsExistence(projectId);
        return taskRepository.findByProjectId(projectId);
    }

}
