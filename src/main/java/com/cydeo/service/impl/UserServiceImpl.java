package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskService taskService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,
                           @Lazy ProjectService projectService, @Lazy TaskService taskService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Override
    public List<UserDTO> getAllUsers() {

        return userRepository.findAll(Sort.by("firstName")).stream()
                .map(userMapper::convertToDto).collect(Collectors.toList());

    }

    @Override
    public UserDTO findByUserName(String username) {

        return userMapper.convertToDto(userRepository.findByUserName(username));

    }

    @Override
    public void save(UserDTO userDTO) {

        userRepository.save(userMapper.convertToEntity(userDTO));

    }

    @Override
    public UserDTO update(UserDTO userDTO) {

        User user = userMapper.convertToEntity(userDTO);

        user.setId(userRepository.findByUserName(userDTO.getUserName()).getId());

        userRepository.save(user);

        return findByUserName(user.getUserName());
    }

    @Override
    public void delete(String username) {

        User user = userRepository.findByUserName(username);

        if (checkIfUserCanBeDeleted(user)) {

            user.setIsDeleted(true);

            user.setUserName(user.getUserName() + "-" + user.getId());

            userRepository.save(user);

        }

    }

    @Override
    public List<UserDTO> getAllByRole(String role) {

        return userRepository.findAllByRoleDescriptionIgnoreCase(role).stream()
                .map(userMapper::convertToDto).collect(Collectors.toList());

    }

    private boolean checkIfUserCanBeDeleted(User user) {

        switch (user.getRole().getDescription()) {

            case "Manager":

                List<ProjectDTO> projectDTOList =
                        projectService.getAllNonCompletedByAssignedManager(userMapper.convertToDto(user));

                return projectDTOList.isEmpty();

            case "Employee":

                List<TaskDTO> taskDTOList =
                        taskService.getAllNonCompletedByAssignedEmployee(userMapper.convertToDto(user));

                return taskDTOList.isEmpty();

            default:

                return true;

        }

    }
}
