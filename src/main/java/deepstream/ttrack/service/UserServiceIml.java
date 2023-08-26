package deepstream.ttrack.service;

import deepstream.ttrack.entity.User;
import deepstream.ttrack.exception.BadRequestException;
import deepstream.ttrack.exception.ErrorParam;
import deepstream.ttrack.exception.Errors;
import deepstream.ttrack.exception.SysError;
import deepstream.ttrack.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceIml implements UserService {

    private final UserRepository userRepository;

    public UserServiceIml(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public Integer getIdByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new BadRequestException(
                new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.USERNAME))));

        return user.getId();
    }
    @Override
    public User getUserByUsername(String username) {
        User optional = userRepository.findByUsername(username).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.ID)))
        );
        return optional;
    }
}
