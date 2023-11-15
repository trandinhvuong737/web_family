package deepstream.ttrack.service;

import deepstream.ttrack.dto.product.ProductMap;
import deepstream.ttrack.dto.user.UserDto;
import deepstream.ttrack.dto.user.UserUpdate;
import deepstream.ttrack.entity.Product;
import deepstream.ttrack.entity.Role;
import deepstream.ttrack.entity.User;
import deepstream.ttrack.exception.BadRequestException;
import deepstream.ttrack.exception.ErrorParam;
import deepstream.ttrack.exception.Errors;
import deepstream.ttrack.exception.SysError;
import deepstream.ttrack.mapper.ProductMapper;
import deepstream.ttrack.repository.ProductRepository;
import deepstream.ttrack.repository.RoleRepository;
import deepstream.ttrack.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceIml implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder encoder;

    public UserServiceIml(UserRepository userRepository, RoleRepository roleRepository, ProductRepository productRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.productRepository = productRepository;
        this.encoder = encoder;
    }
    @Override
    public Integer getIdByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new BadRequestException(
                new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.USERNAME))));

        return user.getId();
    }
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.ID)))
        );
    }

    @Override
    public List<UserDto> getAllUser() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user:users) {
            List<ProductMap> products = ProductMapper.INSTANCE.productMapToProductResponseDtos(user.getProducts());
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setEmail(user.getEmail());
            userDto.setUsername(user.getUsername());
            userDto.setPassword(user.getPassword());
            userDto.setRoleId(user.getRole().getRoleId());
            userDto.setRoleName(user.getRole().getName());
            userDto.setProducts(products);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public void updateUser(UserUpdate userUpdate, int id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.ID)))
        );
        Role role = roleRepository.getRoleByRoleId(userUpdate.getRoleId()).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_ROLE_NOT_FOUND, new ErrorParam(Errors.ROLE_ID)))
        );

        List<Product> products = new ArrayList<>();
        for (ProductMap productMap: userUpdate.getProducts())
        {
            Product product = productRepository.findById(productMap.getProductId()).orElseThrow(
                    () -> new BadRequestException(
                            new SysError(Errors.NOT_FOUND, new ErrorParam(Errors.PRODUCT_ID)))
            );
            products.add(product);
        }


        user.setUsername(userUpdate.getUsername());
        user.setEmail(userUpdate.getEmail() );
        user.setRole(role);
        user.setProducts(products);
        user.setPassword(encoder.encode(userUpdate.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void delete(int userId) {
        User users = userRepository.findById(userId).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.ID)))
        );
        userRepository.delete(users);
    }
}
