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
public class DistrictModel {
    private String name;
    private int code;
    private String codename;
//    @SerializedName("division_type")
    private String division_type;
//    @SerializedName("short_codename")
    private String short_codename;
    private List<WardModel> wards;

}
