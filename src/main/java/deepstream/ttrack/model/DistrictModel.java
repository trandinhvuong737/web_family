package deepstream.ttrack.model;

import deepstream.ttrack.dto.DistrictDto;
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
    private List<DistrictDto> results;
}
