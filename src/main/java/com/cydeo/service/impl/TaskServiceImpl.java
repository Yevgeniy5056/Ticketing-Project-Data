package com.cydeo.service.impl;

import com.cydeo.dto.TaskDTO;
import com.cydeo.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Override
    public List<TaskDTO> getAllTasks() {
        return List.of();
    }

    @Override
    public void save(TaskDTO task) {

    }

    @Override
    public void update(TaskDTO task) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public TaskDTO getById(Long id) {
        return null;
    }
}
