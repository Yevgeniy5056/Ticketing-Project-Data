package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Status;

import java.util.List;

public interface TaskService {

    List<TaskDTO> getAllTasks();

    void save(TaskDTO taskDTO);

    void update(TaskDTO taskDTO);

    void delete(Long id);

    TaskDTO getById(Long id);

    int totalNonCompletedTasks(String code);

    int totalCompletedTasks(String code);

    void deleteByProject(ProjectDTO projectDTO);

    void completeByProject(ProjectDTO projectDTO);

    List<TaskDTO> getAllTasksByStatusIsNot(Status status);

    List<TaskDTO> getAllTasksByStatus(Status status);

    List<TaskDTO> getAllNonCompletedByAssignedEmployee(UserDTO assignedEmployee);
}
