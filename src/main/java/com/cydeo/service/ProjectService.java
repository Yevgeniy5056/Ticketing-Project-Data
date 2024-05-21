package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;

import java.util.List;

public interface ProjectService {

    ProjectDTO getByProjectCode(String code);

    List<ProjectDTO> getAllProjects();

    void save(ProjectDTO projectDTO);

    void update(ProjectDTO projectDTO);

    void delete(String code);

    void complete(String code);
}
