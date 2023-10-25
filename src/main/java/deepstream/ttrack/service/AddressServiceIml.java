package deepstream.ttrack.service;

import deepstream.ttrack.exception.BadRequestException;
import deepstream.ttrack.exception.ErrorParam;
import deepstream.ttrack.exception.SysError;
import deepstream.ttrack.model.CityModel;
import deepstream.ttrack.model.DistrictModel;
import deepstream.ttrack.model.WardModel;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceIml implements AddressService {
    public static final String API_DEPTH_3 = "https://provinces.open-api.vn/api/?depth=3";
    public static final String LIST_NULL = "List null";
    private final WebClient webClient;

    public AddressServiceIml() {
        this.webClient = WebClient.create("");
    }

    @Override
    @Cacheable(value = "getAllCity")
    public List<String> getAllCity() {
        List<CityModel> response = webClient.get()
                .uri(API_DEPTH_3)
                .retrieve()
                .bodyToFlux(CityModel.class)
                .collectList()
                .block();
        if (ObjectUtils.isEmpty(response)) {
            throw new BadRequestException(
                    new SysError(LIST_NULL, new ErrorParam("ward")));
        }
        return response.stream().map(CityModel::getName)
                .collect(Collectors.toList());

    }

    @Override
    @Cacheable(value = "getDistrict", key = "#province")
    public List<String> getDistrict(String province) {
        List<CityModel> response = webClient.get()
                .uri(API_DEPTH_3)
                .retrieve()
                .bodyToFlux(CityModel.class)
                .collectList()
                .block();
        if (ObjectUtils.isEmpty(response)) {
            throw new BadRequestException(
                    new SysError(LIST_NULL, new ErrorParam("district")));
        }
        return response.stream()
                .filter(cityModel -> province.equals(cityModel.getName()))
                .flatMap(cityModel -> cityModel.getDistricts().stream())
                .map(DistrictModel::getName)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "getWard", key = "#province + '-' + #districtName")
    public List<String> getWard(String districtName, String province) {
        List<CityModel> response = webClient.get()
                .uri(API_DEPTH_3)
                .retrieve()
                .bodyToFlux(CityModel.class)
                .collectList()
                .block();
        if (ObjectUtils.isEmpty(response)) {
            throw new BadRequestException(
                    new SysError(LIST_NULL, new ErrorParam("ward")));
        }
        return response.stream()
                .filter(cityModel -> province.equals(cityModel.getName()))
                .flatMap(cityModel -> cityModel.getDistricts().stream())
                .filter(districtTestDTO -> districtName.equals(districtTestDTO.getName()))
                .flatMap(districtTestDTO -> districtTestDTO.getWards().stream())
                .map(WardModel::getName)
                .collect(Collectors.toList());
    }
}
