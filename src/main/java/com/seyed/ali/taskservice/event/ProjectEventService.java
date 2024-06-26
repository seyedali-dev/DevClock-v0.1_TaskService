package com.seyed.ali.taskservice.event;

import com.seyed.ali.taskservice.model.payload.ProjectDTO;
import com.seyed.ali.taskservice.repository.TaskRepository;
import com.seyed.ali.taskservice.service.interfaces.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectEventService {

    private final TaskService taskService;
    private final TaskRepository taskRepository;

    public void handleDeleteOperation(ProjectDTO projectDTO) {
        log.info("\"Delete\" project, associated tasks & time entries: {}", projectDTO);
        this.taskRepository.findByProjectIdOrderByTaskOrderAsc(projectDTO.getProjectId())
                .forEach(this.taskService::deleteTask);
    }

    public void handleDetachOperation(ProjectDTO projectDTO) {
        log.info("\"Detach\" project, associated tasks & time entries: {}", projectDTO);
        this.taskRepository.findByProjectIdOrderByTaskOrderAsc(projectDTO.getProjectId())
                .forEach(task -> {
                    task.setProjectId(null);
                    this.taskRepository.save(task);
                });
    }

}
