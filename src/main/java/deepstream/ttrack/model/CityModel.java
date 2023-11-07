package deepstream.ttrack.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CityModel {

    private String name;
    private Integer code;
    private String codename;
    private String division_type;
    private Integer phone_code;
    private List<DistrictModel> districts;
}
