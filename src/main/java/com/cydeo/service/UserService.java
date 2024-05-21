package com.cydeo.service;

import com.cydeo.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO findByUserName(String username);

    void save(UserDTO userDTO);

    void deleteByUserName(String username);

    UserDTO update(UserDTO userDTO);

    void delete(String username);

    List<UserDTO> getAllByRole(String role);
}
