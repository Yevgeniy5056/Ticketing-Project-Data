package com.cydeo.service.impl;

import com.cydeo.dto.RoleDTO;
import com.cydeo.mapper.MapperUtil;
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
    private final MapperUtil mapperUtil;

    @Override
    public List<RoleDTO> getAllRoles() {

        return roleRepository.findAll().stream()
                .map(role -> mapperUtil.convert(role, RoleDTO.class)).collect(Collectors.toList());

    }

    @Override
    public RoleDTO findById(Long id) {

        return mapperUtil.convert(roleRepository.findById(id).get(), RoleDTO.class);

    }
}
