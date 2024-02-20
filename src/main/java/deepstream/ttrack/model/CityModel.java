package deepstream.ttrack.model;

import deepstream.ttrack.dto.CityDto;
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
    private List<CityDto> results;
}
