package deepstream.ttrack.service;

import deepstream.ttrack.dto.role.RoleDto;
import deepstream.ttrack.dto.role.RoleRequest;
import deepstream.ttrack.entity.Role;
import deepstream.ttrack.exception.BadRequestException;
import deepstream.ttrack.exception.ErrorParam;
import deepstream.ttrack.exception.Errors;
import deepstream.ttrack.exception.SysError;
import deepstream.ttrack.mapper.RoleMapper;
import deepstream.ttrack.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceIml implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceIml(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void addNewRole(RoleRequest roleRequest) {
        Role role = new Role();
        role.setName(roleRequest.getName());
        role.setDescription(roleRequest.getDescription());
        roleRepository.save(role);
    }

    @Override
    public List<RoleDto> getAllRole() {
        List<Role> roleDtos = roleRepository.findAll();
        List<RoleDto> roleResponse = new ArrayList<>();
        roleDtos.stream()
                .map(RoleMapper.INSTANCE::roleToRoleDto)
                .forEach(roleResponse::add);
        return roleResponse;
    }

    @Override
    public void deleteRole(int roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_ROLE_NOT_FOUND, new ErrorParam(Errors.ID)))
        );

        if(role.getName().equals("ROLE_SUPER_ADMIN")){
            throw new BadRequestException(
                    new SysError(Errors.ERROR_ROLE, new ErrorParam(Errors.ID)));
        }
        roleRepository.delete(role);
    }
}
