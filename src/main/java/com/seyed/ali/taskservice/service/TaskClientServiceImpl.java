package com.seyed.ali.taskservice.service;

import com.seyed.ali.taskservice.exceptions.ResourceNotFoundException;
import com.seyed.ali.taskservice.model.domain.Task;
import com.seyed.ali.taskservice.repository.TaskRepository;
import com.seyed.ali.taskservice.service.interfaces.TaskClientService;
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
