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
public class CityDto {
    @JsonProperty("province_id")
    private String provinceId;
    @JsonProperty("province_name")
    private String provinceName;
    @JsonProperty("province_type")
    private String provinceType;

}
