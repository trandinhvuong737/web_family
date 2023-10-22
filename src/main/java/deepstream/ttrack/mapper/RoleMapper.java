package deepstream.ttrack.mapper;

import deepstream.ttrack.dto.product.ProductResponseDto;
import deepstream.ttrack.dto.role.RoleDto;
import deepstream.ttrack.entity.Product;
import deepstream.ttrack.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleDto roleToRoleDto(Role role);
}
