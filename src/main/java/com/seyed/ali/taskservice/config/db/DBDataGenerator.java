package com.seyed.ali.taskservice.config.db;

import com.seyed.ali.taskservice.model.domain.Task;
import com.seyed.ali.taskservice.model.enums.TaskStatus;
import com.seyed.ali.taskservice.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DBDataGenerator {

    private final TaskRepository taskRepository;
    private int counter = 0;

    @Bean
    @Transactional
    public CommandLineRunner commandLineRunner() {
        return args -> {
            this.taskRepository.saveAll(this.generateTasks(2, "1"));
            this.taskRepository.saveAll(this.generateTasks(2, "2"));
        };
    }

    public List<Task> generateTasks(int count, String projectId) {
        List<Task> taskList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Task task = new Task();
            task.setTaskId("" + ++this.counter);
            task.setTaskName("Task " + this.counter);
            task.setTaskDescription("Description for Task " + this.counter);
            task.setTaskStatus(TaskStatus.IN_PROGRESS);
            task.setTaskOrder(i + 1);
            task.setProjectId(projectId);
            taskList.add(task);
        }
        return taskList;
    }

}
