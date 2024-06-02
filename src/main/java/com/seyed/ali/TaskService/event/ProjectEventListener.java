package com.seyed.ali.TaskService.event;

import com.seyed.ali.TaskService.model.payload.ProjectDTO;
import com.seyed.ali.TaskService.repository.TaskRepository;
import com.seyed.ali.TaskService.service.interfaces.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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
            topics = "${spring.kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}" // if we change the group-id name, the events from the previous data will also be logged, otherwise, only the new events will be logged!
    )
    public void listenProject(@Payload ConsumerRecord<String, ProjectDTO> record) {
        ProjectDTO projectDTO = record.value();
        log.info("Received message: {}", projectDTO);

        this.taskRepository.findByProjectIdOrderByTaskOrderAsc(projectDTO.getProjectId())
                .forEach(this.taskService::deleteTask);
    }

}
