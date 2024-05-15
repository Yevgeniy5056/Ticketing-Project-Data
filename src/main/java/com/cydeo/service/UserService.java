package com.cydeo.service;

import com.cydeo.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO findByUserName(String username);

    void save(UserDTO user);

    void deleteByUserName(String username);

    UserDTO update(UserDTO user);

}
