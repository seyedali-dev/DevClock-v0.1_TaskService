package com.seyed.ali.taskservice.service.interfaces;

import com.seyed.ali.taskservice.model.domain.Task;

public interface TaskClientService {

    /**
     * Checks whether the provided task is valid or not.
     *
     * @param taskId the task to check validation.
     * @return {@code true} if the task is valid,
     * otherwise return {@code false}.
     */
    boolean validateTasksExistence(String taskId);

    /**
     * Fetches a task by its name.
     *
     * @param identifier the task name.
     * @return found Task object.
     */
    Task getTaskByName(String identifier);

}
