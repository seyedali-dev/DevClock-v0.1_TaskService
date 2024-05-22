package com.seyed.ali.TaskService.service.interfaces;

import com.seyed.ali.TaskService.model.domain.Task;

import java.util.List;

public interface TaskService {

    Task createTask(Task task);

    List<Task> findAllTasksForProject(String projectId);

}
