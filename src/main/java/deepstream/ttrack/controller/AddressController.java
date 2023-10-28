package deepstream.ttrack.controller;

import deepstream.ttrack.common.constant.Constant;
import deepstream.ttrack.dto.ResponseJson;
import deepstream.ttrack.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/address")
@AllArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/get-all-province")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<List<String>>> getAllCity() {
        List<String> cities = addressService.getAllCity();
        return ResponseEntity.ok().body(
                new ResponseJson<>(cities, HttpStatus.OK, Constant.SUCCESS));

    }

    @GetMapping("/get-all-District/{province}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<List<String>>> getDistrictById(@PathVariable String province) {
        List<String> districts = addressService.getDistrict(province);
        return ResponseEntity.ok().body(
                new ResponseJson<>(districts, HttpStatus.OK, Constant.SUCCESS));

    }
    @GetMapping("/get-all-ward/{provinceName}/{districtName}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<List<String>>> getWard(
            @PathVariable String provinceName,
            @PathVariable String districtName ) {
        List<String> wardResponse = addressService.getWard(districtName, provinceName);
        return ResponseEntity.ok().body(
                new ResponseJson<>(wardResponse, HttpStatus.OK, Constant.SUCCESS));

    }
}