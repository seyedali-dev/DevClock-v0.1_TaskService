package com.seyed.ali.TaskService.event;

import com.seyed.ali.TaskService.repository.TaskRepository;
import com.seyed.ali.TaskService.service.interfaces.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectEventListener {

    private final TaskService taskService;
    private final TaskRepository taskRepository;

    @KafkaListener(
            topics = "${kafka.topic.name}",
            groupId = "${kafka.group-id}"
    )
    public void consumeProjectEvent(@Payload ProjectEvent event) {
        Project project = event.getProject();

        log.info("Received event: {}", event.getMessage());
        log.info("Received project: {}", project);

        this.taskRepository.deleteAll(
                this.taskService.findAllTasksForProject(project.getProjectId())
        );
    }

}

