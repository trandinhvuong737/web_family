package deepstream.ttrack.service;

import deepstream.ttrack.dto.role.RoleDto;
import deepstream.ttrack.dto.role.RoleRequest;

import java.util.List;

public interface RoleService {
    public void addNewRole(RoleRequest roleRequest);
    public List<RoleDto> getAllRole();
    public void deleteRole(int roleId);

}
