package deepstream.ttrack.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdate {
    private String username;
    private String email;
    private String password;
    private int roleId;
}
