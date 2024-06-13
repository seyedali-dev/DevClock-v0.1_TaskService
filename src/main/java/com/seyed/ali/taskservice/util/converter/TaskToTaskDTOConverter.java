package com.seyed.ali.taskservice.util.converter;

import com.seyed.ali.taskservice.model.domain.Task;
import com.seyed.ali.taskservice.model.payload.TaskDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskToTaskDTOConverter implements Converter<Task, TaskDTO> {

    @Override
    public TaskDTO convert(Task source) {
        return new TaskDTO(source.getTaskId(), source.getTaskName(), source.getTaskDescription(), source.getTaskStatus(), source.getTaskOrder(), source.getProjectId());
    }

}
