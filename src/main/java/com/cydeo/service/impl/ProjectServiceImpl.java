package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Override
    public ProjectDTO getByProjectCode(String code) {
        return null;
    }

    @Override
    public List<ProjectDTO> getAllProjects() {
        return List.of();
    }

    @Override
    public void save(ProjectDTO project) {

    }

    @Override
    public void update(ProjectDTO project) {

    }

    @Override
    public void delete(String code) {

    }
}
