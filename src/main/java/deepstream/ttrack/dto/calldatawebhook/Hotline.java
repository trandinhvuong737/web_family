package deepstream.ttrack.dto.calldatawebhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Hotline {

    @JsonProperty("hotline_number")
    private String hotlineNumber;
    @JsonProperty("date_create")
    private String dateCreate;
    @JsonProperty("time_call")
    private String timeCall;
    @JsonProperty("type_call")
    private String typeCall;
    @JsonProperty("disposition")
    private String disposition;
    @JsonProperty("record_file")
    private List<String> recordFiles;
    @JsonProperty("caller")
    private List<Caller> caller;
    @JsonProperty("callee")
    private List<Caller> callee;

}
