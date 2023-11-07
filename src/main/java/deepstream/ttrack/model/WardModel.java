package deepstream.ttrack.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WardModel {
    private String name;
    private int code;
    private String codename;
    @SerializedName("division_type")
    private String divisionType;
    @SerializedName("short_codename")
    private String shortCodename;
}
