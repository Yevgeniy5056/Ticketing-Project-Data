package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final MapperUtil mapperUtil;
    private final UserService userService;


    @Override
    public List<TaskDTO> getAllTasks() {

        return taskRepository.findAll().stream()
                .map(task -> mapperUtil.convert(task, TaskDTO.class)).collect(Collectors.toList());

    }

    @Override
    public void save(TaskDTO taskDTO) {

        taskDTO.setTaskStatus(Status.OPEN);

        taskDTO.setAssignedDate(LocalDate.now());

        taskRepository.save(mapperUtil.convert(taskDTO, Task.class));

    }

    @Override
    public void update(TaskDTO taskDTO) {

        Optional<Task> task = taskRepository.findById(taskDTO.getId());

        Task convertedTask = mapperUtil.convert(taskDTO, Task.class);

        if (task.isPresent()) {

            convertedTask.setTaskStatus(
                    taskDTO.getTaskStatus() == null ? task.get().getTaskStatus() : taskDTO.getTaskStatus());

            convertedTask.setAssignedDate(task.get().getAssignedDate());

            taskRepository.save(convertedTask);

        }

    }

    @Override
    public void delete(Long id) {

        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {

            task.get().setIsDeleted(true);

            taskRepository.save(task.get());

        }

    }

    @Override
    public TaskDTO getById(Long id) {

        return mapperUtil.convert(taskRepository.findById(id).orElseThrow(), TaskDTO.class);

    }

    @Override
    public int totalNonCompletedTasks(String code) {
        return taskRepository.totalNonCompletedTasks(code);
    }

    @Override
    public int totalCompletedTasks(String code) {
        return taskRepository.totalCompletedTasks(code);
    }

    @Override
    public void deleteByProject(ProjectDTO projectDTO) {

        Project project = mapperUtil.convert(projectDTO, Project.class);

        List<Task> tasks = taskRepository.findAllByProject(project);

        tasks.forEach(task -> delete(task.getId()));
    }

    @Override
    public void completeByProject(ProjectDTO projectDTO) {

        Project project = mapperUtil.convert(projectDTO, Project.class);

        List<Task> tasks = taskRepository.findAllByProject(project);

        tasks.stream().map(task -> mapperUtil.convert(task, TaskDTO.class)).forEach(taskDTO -> {
            taskDTO.setTaskStatus(Status.COMPLETE);
            update(taskDTO);
        });
    }

    @Override
    public List<TaskDTO> getAllTasksByStatusIsNot(Status status) {

        UserDTO loggedInUser = userService.findByUserName("john@employee.com");

        List<Task> tasks = taskRepository.
                findAllByTaskStatusIsNotAndAssignedEmployee(status, mapperUtil.convert(loggedInUser, User.class));

        return tasks.stream().map(task -> mapperUtil.convert(task, TaskDTO.class)).collect(Collectors.toList());

    }

    @Override
    public List<TaskDTO> getAllTasksByStatus(Status status) {

        UserDTO loggedInUser = userService.findByUserName("john@employee.com");

        List<Task> tasks = taskRepository.
                findAllByTaskStatusAndAssignedEmployee(status, mapperUtil.convert(loggedInUser, User.class));

        return tasks.stream().map(task -> mapperUtil.convert(task, TaskDTO.class)).collect(Collectors.toList());

    }

    @Override
    public List<TaskDTO> getAllNonCompletedByAssignedEmployee(UserDTO assignedEmployee) {

        List<Task> projects =
                taskRepository.findAllByTaskStatusIsNotAndAssignedEmployee(
                        Status.COMPLETE, mapperUtil.convert(assignedEmployee, User.class));

        return projects.stream().map(task -> mapperUtil.convert(task, TaskDTO.class)).collect(Collectors.toList());
    }


}
