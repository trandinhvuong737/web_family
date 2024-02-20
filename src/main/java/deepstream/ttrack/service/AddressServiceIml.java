package deepstream.ttrack.service;

import deepstream.ttrack.exception.BadRequestException;
import deepstream.ttrack.exception.ErrorParam;
import deepstream.ttrack.exception.SysError;
import deepstream.ttrack.dto.CityDto;
import deepstream.ttrack.dto.DistrictDto;
import deepstream.ttrack.dto.WardDto;
import deepstream.ttrack.model.CityModel;
import deepstream.ttrack.model.DistrictModel;
import deepstream.ttrack.model.WardModel;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.List;

@Service
public class AddressServiceIml implements AddressService {
    public static final String API_PROVINCE = "https://vapi.vnappmob.com/api/province/";
    public static final String LIST_NULL = "Danh sách rỗng";
    private final WebClient webClient;

    public AddressServiceIml() {
        this.webClient = WebClient.builder()
                .baseUrl(API_PROVINCE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create().followRedirect(true)))
                .build();
    }

    @Override
    @Cacheable(value = "getAllCity")
    public List<CityDto> getAllCity() {
        CityModel response = webClient.get()
                .uri(API_PROVINCE)
                .retrieve()
                .bodyToMono(CityModel.class)
                .block();
        if (ObjectUtils.isEmpty(response)) {
            throw new BadRequestException(
                    new SysError(LIST_NULL, new ErrorParam("ward")));
        }
        return response.getResults();

    }

    @Override
    @Cacheable(value = "getDistrict", key = "#districtId")
    public List<DistrictDto> getDistrict(String districtId) {
        DistrictModel response = webClient.get()
                .uri(API_PROVINCE + "district/" + districtId)
                .retrieve()
                .bodyToMono(DistrictModel.class)
                .block();
        if (ObjectUtils.isEmpty(response)) {
            throw new BadRequestException(
                    new SysError(LIST_NULL, new ErrorParam("district")));
        }
        return response.getResults();
    }

    @Override
    @Cacheable(value = "getWard", key = "#districtId")
    public List<WardDto> getWard(String districtId) {
        WardModel response = webClient.get()
                .uri(API_PROVINCE + "ward/" + districtId)

                .retrieve()
                .bodyToMono(WardModel.class)
                .block();
        if (ObjectUtils.isEmpty(response)) {
            throw new BadRequestException(
                    new SysError(LIST_NULL, new ErrorParam("ward")));
        }
        return response.getResults();
    }
}
