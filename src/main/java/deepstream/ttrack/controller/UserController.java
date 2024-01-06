package deepstream.ttrack.controller;

import deepstream.ttrack.common.constant.Constant;
import deepstream.ttrack.dto.ResponseJson;
import deepstream.ttrack.dto.user.UserDto;
import deepstream.ttrack.dto.user.UserUpdate;
import deepstream.ttrack.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/get")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ResponseJson<List<UserDto>>> getAllUser() {
        logger.info("getAllUser");
        List<UserDto> users = userService.getAllUser();
        return ResponseEntity.ok().body(
                new ResponseJson<>(users, HttpStatus.OK, Constant.SUCCESS));

    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ResponseJson<Boolean>> userUpdate(
            @PathVariable int id,
            @RequestBody UserUpdate userUpdate) {
        logger.info("userUpdate");
        userService.updateUser(userUpdate, id);
        return ResponseEntity.ok().body(
                new ResponseJson<>(true, HttpStatus.OK, Constant.UPDATE_USER_SUCCESS));

    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ResponseJson<Boolean>> getAllUser(@PathVariable int userId) {
        logger.info("getAllUser");
        userService.delete(userId);
        return ResponseEntity.ok().body(
                new ResponseJson<>(true, HttpStatus.OK, Constant.DELETE_USER_SUCCESS));

    }

}
