package com.seyed.ali.TaskService.service;

import com.seyed.ali.TaskService.exceptions.ResourceNotFoundException;
import com.seyed.ali.TaskService.model.domain.Task;
import com.seyed.ali.TaskService.repository.TaskRepository;
import com.seyed.ali.TaskService.service.interfaces.TaskClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskClientServiceImpl implements TaskClientService {

    private final TaskRepository taskRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateTasksExistence(String taskId) {
        Task task = this.taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Provided task id " + taskId + " was not found."));
        return task != null;
    }

}
