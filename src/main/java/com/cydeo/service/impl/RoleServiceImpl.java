package com.cydeo.service.impl;

import com.cydeo.dto.RoleDTO;
import com.cydeo.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Override
    public List<RoleDTO> getAllRoles() {
        return List.of();
    }

    @Override
    public RoleDTO findById(Long id) {
        return null;
    }
}
