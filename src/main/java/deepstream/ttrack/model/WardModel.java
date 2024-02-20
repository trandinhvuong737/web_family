package deepstream.ttrack.model;

import deepstream.ttrack.dto.WardDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WardModel {
    private List<WardDto> results;
}
