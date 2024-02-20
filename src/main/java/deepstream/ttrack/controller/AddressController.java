package deepstream.ttrack.controller;

import deepstream.ttrack.common.constant.Constant;
import deepstream.ttrack.dto.ResponseJson;
import deepstream.ttrack.dto.CityDto;
import deepstream.ttrack.dto.DistrictDto;
import deepstream.ttrack.dto.WardDto;
import deepstream.ttrack.service.AddressService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/address")
@AllArgsConstructor
public class AddressController {
    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);
    private final AddressService addressService;

    @GetMapping("/get-all-province")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<List<CityDto>>> getAllCity() {
        logger.info("get-all-province");
        List<CityDto> cities = addressService.getAllCity();
        return ResponseEntity.ok().body(
                new ResponseJson<>(cities, HttpStatus.OK, Constant.SUCCESS));

    }

    @GetMapping("/get-all-District/{provinceId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<List<DistrictDto>>> getDistrictById(@PathVariable String provinceId) {
        logger.info("get-all-District");
        List<DistrictDto> districts = addressService.getDistrict(provinceId);
        return ResponseEntity.ok().body(
                new ResponseJson<>(districts, HttpStatus.OK, Constant.SUCCESS));

    }
    @GetMapping("/get-all-ward/{districtId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<List<WardDto>>> getWard(
            @PathVariable String districtId ) {
        logger.info("get-all-ward ");
        List<WardDto> wardResponse = addressService.getWard(districtId);
        return ResponseEntity.ok().body(
                new ResponseJson<>(wardResponse, HttpStatus.OK, Constant.SUCCESS));

    }
}
