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
public class Caller {
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("vbot_id")
    private String vbotId;
    @JsonProperty("date_create")
    private String dateCreate;
    @JsonProperty("disposition")
    private String disposition;
    @JsonProperty("time_call")
    private String timeCall;
    @JsonProperty("postage")
    private double postage;
    @JsonProperty("source")
    private String source;
    @JsonProperty("member_no")
    private String memberNo;
}
