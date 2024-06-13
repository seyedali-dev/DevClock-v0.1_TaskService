package com.seyed.ali.taskservice.model.domain;

import com.seyed.ali.taskservice.model.enums.TaskStatus;
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
    private Integer taskOrder;

    private String projectId;

}
