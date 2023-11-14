package deepstream.ttrack.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import deepstream.ttrack.dto.product.ProductMap;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class UserDto {
    private int id;
    private String username;
    private String email;
    private String password;
    private Integer roleId;
    private String roleName;
    private List<ProductMap> products;
}
