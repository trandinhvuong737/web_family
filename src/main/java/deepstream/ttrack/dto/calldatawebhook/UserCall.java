package deepstream.ttrack.dto.calldatawebhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCall {
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("full_name_unsigned")
    private String fullNameUnsigned;
}
