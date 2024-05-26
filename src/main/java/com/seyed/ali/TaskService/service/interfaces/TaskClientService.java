package com.seyed.ali.TaskService.service.interfaces;

public interface TaskClientService {

    /**
     * Checks whether the provided task is valid or not.
     *
     * @param taskId the task to check validation.
     * @return {@code true} if the task is valid,
     * otherwise return {@code false}.
     */
    boolean validateTasksExistence(String taskId);

}
