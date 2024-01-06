package deepstream.ttrack.controller;


import deepstream.ttrack.common.constant.Constant;
import deepstream.ttrack.common.utils.JwtUtils;
import deepstream.ttrack.dto.JwtResponse;
import deepstream.ttrack.dto.LoginRequest;
import deepstream.ttrack.dto.ResponseJson;
import deepstream.ttrack.dto.SignupRequest;
import deepstream.ttrack.dto.product.ProductMap;
import deepstream.ttrack.entity.Product;
import deepstream.ttrack.entity.Role;
import deepstream.ttrack.entity.User;
import deepstream.ttrack.exception.BadRequestException;
import deepstream.ttrack.exception.ErrorParam;
import deepstream.ttrack.exception.Errors;
import deepstream.ttrack.exception.SysError;
import deepstream.ttrack.repository.ProductRepository;
import deepstream.ttrack.repository.RoleRepository;
import deepstream.ttrack.repository.UserRepository;
import deepstream.ttrack.service.UserService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final JwtUtils jwtUtils;

    private final UserService userService;

    private final PasswordEncoder encoder;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);



    @PostMapping("/signin")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ResponseJson<JwtResponse>> authenticateUser(@Validated @RequestBody LoginRequest loginRequest) {
        logger.info("authenticateUser");
        if (!userRepository.existsByUsername(loginRequest.getUsername())) {
            throw new BadRequestException(
                    new SysError(Errors.ERROR_USER_NAME_OR_PASSWORD_NOT_FOUND, new ErrorParam(Errors.USER)));
        }
        User userLogin = userService.getUserByUsername(loginRequest.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLogin.getUsername(),
                    loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            return ResponseEntity.ok().body(new ResponseJson<>(new JwtResponse(jwt,
                jwtUtils.getExpiration(jwt),
                userLogin.getId(),
                userLogin.getUsername(),
                userLogin.getEmail()),
                HttpStatus.OK, Constant.SUCCESS));
        }catch (Exception e){
            throw new BadRequestException(
                new SysError(Errors.ERROR_USER_NAME_OR_PASSWORD_NOT_FOUND, new ErrorParam(Errors.USER)));
        }
    }

    @PostMapping("/signup")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ResponseJson<Boolean>> registerUser(@Validated @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new BadRequestException(new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.USERNAME)));
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new BadRequestException(new SysError(Errors.ERROR_EMAIL_NOT_FOUND, new ErrorParam(Errors.EMAIL)));
        }

        Role role = roleRepository.findById(signupRequest.getRoleId()).orElseThrow(
                () -> new BadRequestException(new SysError(Errors.NOT_FOUND, new ErrorParam(Errors.ROLE))));

        List<Product> productList = new ArrayList<>();
        for (ProductMap productMap:signupRequest.getProducts()
             ) {
            Product product =productRepository.findById(productMap.getProductId()).orElseThrow(
                    () -> new BadRequestException(new SysError(Errors.NOT_FOUND, new ErrorParam(Errors.PRODUCT))));
            productList.add(product);
        }

        User user =  User.builder()
            .username(signupRequest.getUsername())
            .email(signupRequest.getEmail())
            .createDate(LocalDateTime.now())
            .status(Constant.ACTIVE)
            .role(role)
            .products(productList)
            .password(encoder.encode(signupRequest.getPassword()))
            .build();

        userRepository.save(user);

        return ResponseEntity.ok().body(new ResponseJson<>(Boolean.TRUE, HttpStatus.OK, Constant.ADD_USER_SUCCESS));
    }

}

