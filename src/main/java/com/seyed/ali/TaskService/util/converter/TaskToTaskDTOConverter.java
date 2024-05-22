package com.seyed.ali.TaskService.util.converter;

import com.seyed.ali.TaskService.model.domain.Task;
import com.seyed.ali.TaskService.model.payload.TaskDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskToTaskDTOConverter implements Converter<Task, TaskDTO> {

    @Override
    public TaskDTO convert(Task source) {
        return new TaskDTO(source.getTaskId(), source.getTaskName(), source.getTaskDescription(), source.getTaskStatus(), source.getProjectId());
    }

}
