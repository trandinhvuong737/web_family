package deepstream.ttrack.mapper;

import deepstream.ttrack.dto.user.UserDto;
import deepstream.ttrack.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto UserToUserDto(User user);
}
