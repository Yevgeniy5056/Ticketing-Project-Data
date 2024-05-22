package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
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
    private final TaskMapper taskMapper;
    private final ProjectMapper projectMapper;

    @Override
    public List<TaskDTO> getAllTasks() {

        return taskRepository.findAll().stream()
                .map(taskMapper::convertToDto).collect(Collectors.toList());

    }

    @Override
    public void save(TaskDTO taskDTO) {

        taskDTO.setTaskStatus(Status.OPEN);

        taskDTO.setAssignedDate(LocalDate.now());

        taskRepository.save(taskMapper.convertToEntity(taskDTO));

    }

    @Override
    public void update(TaskDTO taskDTO) {

        Optional<Task> task = taskRepository.findById(taskDTO.getId());

        Task convertedTask = taskMapper.convertToEntity(taskDTO);

        if (task.isPresent()) {

            convertedTask.setTaskStatus(task.get().getTaskStatus());

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

        return taskMapper.convertToDto(taskRepository.findById(id).orElseThrow());

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

        Project project = projectMapper.convertToEntity(projectDTO);

        List<Task> tasks = taskRepository.findAllByProject(project);

        tasks.forEach(task -> delete(task.getId()));
    }

    @Override
    public void completeByProject(ProjectDTO projectDTO) {

        Project project = projectMapper.convertToEntity(projectDTO);

        List<Task> tasks = taskRepository.findAllByProject(project);

        tasks.stream().map(taskMapper::convertToDto).forEach(taskDTO -> {
                    taskDTO.setTaskStatus(Status.COMPLETE);
                    update(taskDTO);
                });
    }
}
