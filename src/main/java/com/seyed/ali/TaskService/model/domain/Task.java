package com.seyed.ali.TaskService.model.domain;

import com.seyed.ali.TaskService.model.enums.TaskStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {

    @Id
    private String taskId;

    private String taskName;
    private String taskDescription;
    private TaskStatus taskStatus;

    private String projectId;

}
