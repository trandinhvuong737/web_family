package deepstream.ttrack.controller;

import deepstream.ttrack.common.constant.Constant;
import deepstream.ttrack.dto.ResponseJson;
import deepstream.ttrack.dto.product.ProductResponseDto;
import deepstream.ttrack.dto.role.RoleDto;
import deepstream.ttrack.dto.role.RoleRequest;
import deepstream.ttrack.entity.Role;
import deepstream.ttrack.service.ProductService;
import deepstream.ttrack.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@AllArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/get")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ResponseJson<List<RoleDto>>> getAllRole() {
        List<RoleDto> role = roleService.getAllRole();
        return ResponseEntity.ok().body(new ResponseJson<>(role, HttpStatus.OK, Constant.SUCCESS));

    }

    @PostMapping("/add-new-role")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ResponseJson<Boolean>> addNewRole(
            @RequestBody RoleRequest roleRequest) {
        roleService.addNewRole(roleRequest);
        return ResponseEntity.ok().body(new ResponseJson<>(Boolean.TRUE, HttpStatus.OK, Constant.SUCCESS));

    }

    @DeleteMapping("/delete-role/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ResponseJson<Boolean>> deleteRole(
            @PathVariable int id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok().body(new ResponseJson<>(Boolean.TRUE, HttpStatus.OK, Constant.SUCCESS));

    }
}
