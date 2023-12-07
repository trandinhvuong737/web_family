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
public class CreatedByCall {
    @JsonProperty("contact_id")
    private String contactId;
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
}
