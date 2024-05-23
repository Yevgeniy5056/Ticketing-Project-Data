package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;
    private final ProjectService projectService;
    private final TaskService taskService;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil,
                           @Lazy ProjectService projectService, @Lazy TaskService taskService) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Override
    public List<UserDTO> getAllUsers() {

        return userRepository.findAllByIsDeletedOrderByFirstNameDesc(false).stream()
                .map(user -> mapperUtil.convert(user, UserDTO.class)).collect(Collectors.toList());

    }

    @Override
    public UserDTO findByUserName(String username) {

        return mapperUtil.convert(userRepository.findByUserNameAndIsDeleted(username, false), UserDTO.class);

    }

    @Override
    public void save(UserDTO userDTO) {

        userRepository.save(mapperUtil.convert(userDTO, User.class));

    }

    @Override
    public UserDTO update(UserDTO userDTO) {

        User user = userRepository.findByUserNameAndIsDeleted(userDTO.getUserName(), false);

        User convertedUser = mapperUtil.convert(userDTO, User.class);

        convertedUser.setId(user.getId());

        userRepository.save(user);

        return findByUserName(user.getUserName());
    }

    @Override
    public void delete(String username) {

        User user = userRepository.findByUserNameAndIsDeleted(username, false);

        if (checkIfUserCanBeDeleted(user)) {

            user.setIsDeleted(true);

            user.setUserName(user.getUserName() + "-" + user.getId());

            userRepository.save(user);

        }

    }

    @Override
    public List<UserDTO> getAllByRole(String role) {

        return userRepository.findAllByRoleDescriptionIgnoreCaseAndIsDeleted(role, false).stream()
                .map(user -> mapperUtil.convert(user, UserDTO.class)).collect(Collectors.toList());

    }

    private boolean checkIfUserCanBeDeleted(User user) {

        switch (user.getRole().getDescription()) {

            case "Manager":

                List<ProjectDTO> projectDTOList =
                        projectService.getAllNonCompletedByAssignedManager(mapperUtil.convert(user, UserDTO.class));

                return projectDTOList.isEmpty();

            case "Employee":

                List<TaskDTO> taskDTOList =
                        taskService.getAllNonCompletedByAssignedEmployee(mapperUtil.convert(user, UserDTO.class));

                return taskDTOList.isEmpty();

            default:

                return true;

        }

    }
}
