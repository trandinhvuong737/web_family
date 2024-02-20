package deepstream.ttrack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WardDto {
    @JsonProperty("district_id")
    private String districtId;
    @JsonProperty("ward_id")
    private String wardId;
    @JsonProperty("ward_name")
    private String wardName;
    @JsonProperty("ward_type")
    private String wardType;

}
