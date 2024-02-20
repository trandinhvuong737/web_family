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
public class DistrictDto {
    @JsonProperty("district_id")
    private String districtId;
    @JsonProperty("district_name")
    private String districtName;
    @JsonProperty("district_type")
    private String districtType;
    @JsonProperty("lat")
    private String lat;
    @JsonProperty("lng")
    private String lng;
    @JsonProperty("province_id")
    private String provinceId;

}
