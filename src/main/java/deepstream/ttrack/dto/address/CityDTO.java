package deepstream.ttrack.dto.address;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CityDTO {
    private Long id;
    private String name;
    private Integer code;
    private String codename;
    @SerializedName("division_type")
    private String divisionType;
    @SerializedName("phone_code")
    private Integer phoneCode;
//    private List<DistrictTestDTO> districts;
}
