package deepstream.ttrack.dto.user;

import deepstream.ttrack.dto.product.ProductMap;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserUpdate {
    private String username;
    private String email;
    private String password;
    private int roleId;
    private List<ProductMap> product;
}
