package deepstream.ttrack.service;

import deepstream.ttrack.dto.CityDto;
import deepstream.ttrack.dto.DistrictDto;
import deepstream.ttrack.dto.WardDto;

import java.util.List;

public interface AddressService {
    List<CityDto> getAllCity();

    List<DistrictDto> getDistrict(String province);

    List<WardDto> getWard(String districtId);
}
