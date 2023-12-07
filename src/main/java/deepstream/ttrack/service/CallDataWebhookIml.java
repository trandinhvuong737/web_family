package deepstream.ttrack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import deepstream.ttrack.dto.calldatawebhook.CallDTO;
import deepstream.ttrack.entity.MissedCall;
import deepstream.ttrack.model.MissedCallModel;
import deepstream.ttrack.model.TokenModel;
import deepstream.ttrack.repository.CallDataWebhookRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class CallDataWebhookIml implements CallDataWebhookService{

    public static final String ASIA_HO_CHI_MINH = "Asia/Ho_Chi_Minh";
    private final CallDataWebhookRepository callDataWebhookRepository;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public CallDataWebhookIml(CallDataWebhookRepository callDataWebhookRepository, WebClient.Builder webClient, ObjectMapper objectMapper) {
        this.callDataWebhookRepository = callDataWebhookRepository;
        this.webClient = webClient.baseUrl("").build();
        this.objectMapper = objectMapper;
    }

    @Override
    public void addCallInformation(String id, CallDTO callDTO){
        MissedCall missedCall = new MissedCall();
        missedCall.setTransactionId(id);
        missedCall.setDirection(callDTO.getDirection());
        missedCall.setDestinationNumber(callDTO.getDestinationNumber());
        missedCall.setCreatedDate(callDTO.getCreatedDate());
        missedCall.setSourceNumber(callDTO.getSourceNumber());
        missedCall.setStatus(false);
        callDataWebhookRepository.save(missedCall);
    }

    @Override
    public List<MissedCall> getAllMissCall(){
        return callDataWebhookRepository.findAllMissCall();
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void deleteCallMiss() {
        LocalDateTime endDate = LocalDateTime.now(ZoneId.of(ASIA_HO_CHI_MINH));
        LocalDateTime startDate = endDate.minusDays(60);
        long milliseconds = startDate.atZone(ZoneId.of(ASIA_HO_CHI_MINH)).toInstant().toEpochMilli();
        callDataWebhookRepository.deleteMissCallByDate(milliseconds);
    }

    private MissedCallModel getMissedCallModel(Instant startInstant, Instant endInstant, TokenModel token) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl("https://public-v1-stg.vcontact.services/api/call_transaction/list")
                .queryParam("disposition", "cancelled")
                .queryParam("direction", "inbound")
                .queryParam("page", 1)
                .queryParam("size", 50)
                .queryParam("from_date", startInstant.toEpochMilli())
                .queryParam("to_date", endInstant.toEpochMilli());

        return webClient.get()
                .uri(builder.toUriString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getPayload().getAccessToken())
                .retrieve()
                .bodyToMono(Object.class)
                .map(jsonNode -> objectMapper.convertValue(jsonNode, MissedCallModel.class))
                .block();
    }

    private TokenModel getTokenModel() {
        UriComponentsBuilder tokenBuilder = UriComponentsBuilder
                .fromHttpUrl("https://public-v1-stg.vcontact.services/api/auth")
                .queryParam("apiKey", "D00018F49A543DA4ED33F5B409C207747C1FE6394FE71020F88FA914D1CF0866");

        return webClient.get()
                .uri(tokenBuilder.toUriString())
                .retrieve()
                .bodyToMono(Object.class)
                .map(jsonNode -> objectMapper.convertValue(jsonNode, TokenModel.class))
                .block();
    }
}
