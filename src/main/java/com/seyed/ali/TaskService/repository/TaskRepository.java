package com.seyed.ali.TaskService.repository;

import com.seyed.ali.TaskService.model.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, String> {
}