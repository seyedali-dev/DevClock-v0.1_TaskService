package com.seyed.ali.TaskService.event;

import com.seyed.ali.TaskService.model.payload.ProjectDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectEventListener {

    @KafkaListener(
            topics = "${spring.kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}" // if we change the group-id name, the events from the previous data will also be logged, otherwise, only the new events will be logged!
    )
    public void listenProject(ProjectDTO projectDTO) {
        log.info("Received message: {}", projectDTO);
    }

}
