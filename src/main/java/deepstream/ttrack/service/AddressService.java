package deepstream.ttrack.service;

import java.util.List;

public interface AddressService {
    List<String> getAllCity();

    List<String> getDistrict(String province);

    List<String> getWard(String districtName, String province);
}
