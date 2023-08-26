package deepstream.ttrack.service;

import deepstream.ttrack.entity.User;

public interface UserService {

    Integer getIdByUsername(String username);

    User getUserByUsername(String username);
}
