package deepstream.ttrack.controller;

import deepstream.ttrack.common.constant.Constant;
import deepstream.ttrack.dto.ResponseJson;
import deepstream.ttrack.dto.user.UserDto;
import deepstream.ttrack.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ResponseJson<List<UserDto>>> getAllUser() {
        List<UserDto> users = userService.getAllUser();
        return ResponseEntity.ok().body(
                new ResponseJson<>(users, HttpStatus.OK, Constant.SUCCESS));

    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ResponseJson<Boolean>> getAllUser(
            @PathVariable int id,
            @RequestBody UserDto userDto) {
        userService.updateUser(userDto,id);
        return ResponseEntity.ok().body(
                new ResponseJson<>(true, HttpStatus.OK, Constant.SUCCESS));

    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ResponseJson<Boolean>> getAllUser(@PathVariable int userId) {
        userService.delete(userId);
        return ResponseEntity.ok().body(
                new ResponseJson<>(true, HttpStatus.OK, Constant.SUCCESS));

    }

}
