package deepstream.ttrack.service;

import deepstream.ttrack.dto.user.UserDto;
import deepstream.ttrack.entity.User;

import java.util.List;

public interface UserService {

    Integer getIdByUsername(String username);

    User getUserByUsername(String username);

    List<UserDto> getAllUser();

    void updateUser(UserDto userDto);

    void delete(int userId);



}
