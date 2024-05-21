package com.cydeo.service.impl;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

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
    public void deleteByUserName(String username) {

        userRepository.deleteByUserName(username);

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

        user.setIsDeleted(true);

        userRepository.save(user);

    }

    @Override
    public List<UserDTO> getAllByRole(String role) {

        return userRepository.findAllByRoleDescriptionIgnoreCase(role).stream()
                .map(userMapper::convertToDto).collect(Collectors.toList());

    }
}
