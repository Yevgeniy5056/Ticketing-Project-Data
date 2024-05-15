package com.cydeo.service.impl;

import com.cydeo.dto.RoleDTO;
import com.cydeo.mapper.RoleMapper;
import com.cydeo.repository.RoleRepository;
import com.cydeo.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<RoleDTO> getAllRoles() {

        return roleRepository.findAll().stream().map(roleMapper::convertToDto).collect(Collectors.toList());

    }

    @Override
    public RoleDTO findById(Long id) {

        return roleMapper.convertToDto(roleRepository.findById(id).get());

    }
}
