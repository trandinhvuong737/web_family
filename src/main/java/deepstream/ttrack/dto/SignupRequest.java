package deepstream.ttrack.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class SignupRequest {

    @NotBlank(message = "Username is required.")
    private String username;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email not formatted correctly")
    private String email;

    @NotBlank(message = "First Name is required.")
    private String firstname;

    @NotBlank(message = "Last Name is required.")
    private String lastname;

    @NotBlank(message = "password is required.")
    private String password;

    @NotBlank(message = "Role is required.")
    private int roleId;

}

