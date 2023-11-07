package deepstream.ttrack.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class JwtResponse {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String type = "Bearer";
    @JsonProperty("expired_time")
    private long expiredTime ;
    private int id;
    private String username;
    private String email;
    private String role;

    public JwtResponse(String accessToken, long expiredTime, int id, String username,
        String email) {
        this.accessToken = accessToken;
        this.expiredTime  = expiredTime;
        this.id = id;
        this.username = username;
        this.email = email;
    }

}
