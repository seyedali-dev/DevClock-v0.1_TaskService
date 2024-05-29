package com.seyed.ali.TaskService.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    private String projectId;
    private String projectName;
    private String projectDescription;

}
