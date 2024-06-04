package com.seyed.ali.TaskService.event;

import com.seyed.ali.TaskService.model.enums.OperationType;
import com.seyed.ali.TaskService.model.payload.ProjectDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectEventListener {

    private final ProjectEventService projectEventService;

    @KafkaListener(
            topics = "${spring.kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}" // if we change the group-id name, the events from the previous data will also be logged, otherwise, only the new events will be logged!
    )
    public void handleProjectEvent(@Payload ConsumerRecord<String, ProjectDTO> record, @Header("OperationType") String operationType) {
//    public void handleProjectEvent(@Payload ConsumerRecord<String, ProjectDTO> record, @Headers MessageHeaders messageHeaders) {
//        messageHeaders.forEach((key, value) -> log.info("Received event for key: {}, value: {}", key, value));
        ProjectDTO projectDTO = record.value();
        switch (OperationType.valueOf(operationType)) {
            case DELETE -> this.projectEventService.handleDeleteOperation(projectDTO);
            case DETACH -> this.projectEventService.handleDetachOperation(projectDTO);
            default -> log.warn("\"{}\" operation type not supported.", operationType);
        }
    }

}
