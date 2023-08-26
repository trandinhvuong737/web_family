package deepstream.ttrack.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest() {
        super();
    }

    public LoginRequest(String email, String password) {
        super();
        this.username = email;
        this.password = password;
    }

}

