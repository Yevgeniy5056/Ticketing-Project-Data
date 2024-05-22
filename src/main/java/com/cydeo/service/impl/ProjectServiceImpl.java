package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TaskService taskService;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, UserService userService, UserMapper userMapper, TaskService taskService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.taskService = taskService;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {

        return projectMapper.convertToDto(projectRepository.findByProjectCode(code));

    }

    @Override
    public List<ProjectDTO> getAllProjects() {

        return projectRepository.findAll(Sort.by("projectCode")).stream()
                .map(projectMapper::convertToDto).collect(Collectors.toList());

    }

    @Override
    public void save(ProjectDTO projectDTO) {

        projectDTO.setProjectStatus(Status.OPEN);

        projectRepository.save(projectMapper.convertToEntity(projectDTO));

    }

    @Override
    public void update(ProjectDTO projectDTO) {

        Project convertedProject = projectMapper.convertToEntity(projectDTO);

        convertedProject.setId(projectRepository.findByProjectCode(projectDTO.getProjectCode()).getId());

        convertedProject.setProjectStatus(projectDTO.getProjectStatus());

        projectRepository.save(convertedProject);

    }

    @Override
    public void delete(String code) {

        Project project = projectRepository.findByProjectCode(code);

        project.setIsDeleted(true);

        project.setProjectCode(project.getProjectCode() + "-" + project.getId());

        projectRepository.save(project);

        taskService.deleteByProject(projectMapper.convertToDto(project));

    }

    @Override
    public void complete(String code) {

        Project project = projectRepository.findByProjectCode(code);

        project.setProjectStatus(Status.COMPLETE);

        projectRepository.save(project);

    }

    @Override
    public List<ProjectDTO> getAllProjectDetails() {

        UserDTO currentUserDTO = userService.findByUserName("harold@manager.com");

        User user = userMapper.convertToEntity(currentUserDTO);

        List<Project> projects = projectRepository.findAllByAssignedManager(user);


        return projects.stream().map(project -> {

            ProjectDTO projectDTO = projectMapper.convertToDto(project);

            projectDTO.setUnfinishedTaskCounts(taskService.totalNonCompletedTasks(project.getProjectCode()));
            projectDTO.setCompleteTaskCounts(taskService.totalCompletedTasks(project.getProjectCode()));

            return projectDTO;

        }).collect(Collectors.toList());

    }


}
