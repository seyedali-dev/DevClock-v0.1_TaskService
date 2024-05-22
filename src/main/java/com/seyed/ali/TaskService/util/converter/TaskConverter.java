package com.seyed.ali.TaskService.util.converter;

import com.seyed.ali.TaskService.model.domain.Task;
import com.seyed.ali.TaskService.model.payload.TaskDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskConverter {

    private final TaskDTOToTaskConverter taskDTOToTaskConverter;
    private final TaskToTaskDTOConverter taskToTaskDTOConverter;

    public Task convertToTask(TaskDTO taskDTO) {
        return this.taskDTOToTaskConverter.convert(taskDTO);
    }

    public TaskDTO convertToTaskDTO(Task createdTask) {
        return this.taskToTaskDTOConverter.convert(createdTask);
    }

}
