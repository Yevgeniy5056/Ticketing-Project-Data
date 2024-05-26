package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.MapperUtil;
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
    private final UserService userService;
    private final TaskService taskService;
    private final MapperUtil mapperUtil;

    public ProjectServiceImpl(ProjectRepository projectRepository, UserService userService,
                              TaskService taskService, MapperUtil mapperUtil) {
        this.projectRepository = projectRepository;
        this.userService = userService;
        this.taskService = taskService;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {

        return mapperUtil.convert(projectRepository.findByProjectCode(code), ProjectDTO.class);

    }

    @Override
    public List<ProjectDTO> getAllProjects() {

        return projectRepository.findAll(Sort.by("projectCode")).stream()
                .map(project -> mapperUtil.convert(project, ProjectDTO.class)).collect(Collectors.toList());

    }

    @Override
    public void save(ProjectDTO projectDTO) {

        projectDTO.setProjectStatus(Status.OPEN);

        projectRepository.save(mapperUtil.convert(projectDTO, Project.class));

    }

    @Override
    public void update(ProjectDTO projectDTO) {

        Project project = projectRepository.findByProjectCode(projectDTO.getProjectCode());

        Project convertedProject = mapperUtil.convert(projectDTO, Project.class);

        convertedProject.setId(project.getId());

        convertedProject.setProjectStatus(projectDTO.getProjectStatus());

        projectRepository.save(convertedProject);

    }

    @Override
    public void delete(String code) {

        Project project = projectRepository.findByProjectCode(code);

        project.setIsDeleted(true);

        project.setProjectCode(project.getProjectCode() + "-" + project.getId());

        projectRepository.save(project);

        taskService.deleteByProject(mapperUtil.convert(project, ProjectDTO.class));

    }

    @Override
    public void complete(String code) {

        Project project = projectRepository.findByProjectCode(code);

        project.setProjectStatus(Status.COMPLETE);

        projectRepository.save(project);

        taskService.completeByProject(mapperUtil.convert(project, ProjectDTO.class));

    }

    @Override
    public List<ProjectDTO> getAllProjectDetails() {

        UserDTO currentUserDTO = userService.findByUserName("harold@manager.com");

        User user = mapperUtil.convert(currentUserDTO, User.class);

        List<Project> projects = projectRepository.findAllByAssignedManager(user);


        return projects.stream().map(project -> {

            ProjectDTO projectDTO = mapperUtil.convert(project, ProjectDTO.class);

            projectDTO.setUnfinishedTaskCounts(taskService.totalNonCompletedTasks(project.getProjectCode()));

            projectDTO.setCompleteTaskCounts(taskService.totalCompletedTasks(project.getProjectCode()));

            return projectDTO;

        }).collect(Collectors.toList());

    }

    @Override
    public List<ProjectDTO> getAllNonCompletedByAssignedManager(UserDTO assignedManager) {

        List<Project> projects =
                projectRepository.getAllByProjectStatusIsNotAndAssignedManager(
                        Status.COMPLETE, mapperUtil.convert(assignedManager, User.class));

        return projects.stream().map(project -> mapperUtil.convert(project, ProjectDTO.class)).collect(Collectors.toList());

    }


}
