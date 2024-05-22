package com.seyed.ali.TaskService.service.interfaces;

import com.seyed.ali.TaskService.exceptions.OperationNotSupportedException;
import com.seyed.ali.TaskService.exceptions.ResourceNotFoundException;
import com.seyed.ali.TaskService.model.domain.Task;

import java.util.List;

public interface TaskService {

    /**
     * Creates a new task.
     * If task order is not provided, it will be assigned based on the current maximum order for the project.
     * <p><br/>
     * Validates projectId existence before saving the task.
     * <p>
     * Ensures that there are no duplicate task orders within the same project.
     *
     * @param task The task to create.
     * @return The created task.
     * @throws OperationNotSupportedException If the task order already exists for the project.
     */
    Task createTask(Task task);

    /**
     * Retrieves all tasks for a given project ID, ordered by task order in ascending order.
     * <p>
     * Validates the existence of the project ID before fetching tasks.
     *
     * @param projectId The unique identifier of the project.
     * @return A list of tasks associated with the specified project, ordered by task order.
     * @throws ResourceNotFoundException if the project ID does not exist.
     */
    List<Task> findAllTasksForProject(String projectId);

}
