package deepstream.ttrack.service;

import deepstream.ttrack.dto.calldatawebhook.CallDTO;
import deepstream.ttrack.dto.calldatawebhook.Hotline;
import deepstream.ttrack.entity.CallHistory;

import deepstream.ttrack.repository.CallDataWebhookRepository;
import org.apache.commons.lang3.ObjectUtils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class CallDataWebhookIml implements CallDataWebhookService{

    public static final String ASIA_HO_CHI_MINH = "Asia/Ho_Chi_Minh";
    public static final String MISSCALL = "misscall";
    private final CallDataWebhookRepository callDataWebhookRepository;


    public CallDataWebhookIml(CallDataWebhookRepository callDataWebhookRepository) {
        this.callDataWebhookRepository = callDataWebhookRepository;
    }

    @Override
    public void addCallInformation(CallDTO callDTO){
        if (MISSCALL.equalsIgnoreCase(callDTO.getData().getTypeCall())) {
            CallHistory callHistory = new CallHistory();
            Hotline hotline = callDTO.getData();
            callHistory.setTypeCall(hotline.getTypeCall().toUpperCase());
            callHistory.setStatus(false);
            callHistory.setCreatedDate(callDTO.getTime()*1000);
            callHistory.setDisposition(hotline.getDisposition());
            callHistory.setCallerPhone(hotline.getCaller().get(0).getPhone());
            if (ObjectUtils.isNotEmpty(hotline.getCallee())) {
                callHistory.setCallerPhone(hotline.getCaller().get(0).getPhone());
            }
            callHistory.setStatus(false);
            callHistory.setHotlineNumber(hotline.getHotlineNumber());
            callDataWebhookRepository.save(callHistory);
        }

    }

    @Override
    public List<CallHistory> getAllMissCall(){
        return callDataWebhookRepository.findAllMissCall();
    }

    @Override
    public Integer totalMissCall() {
        return callDataWebhookRepository.getTotalMissCall();
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void deleteCallMiss() {
        LocalDateTime endDate = LocalDateTime.now(ZoneId.of(ASIA_HO_CHI_MINH));
        LocalDateTime startDate = endDate.minusDays(60);
        long milliseconds = startDate.atZone(ZoneId.of(ASIA_HO_CHI_MINH)).toInstant().toEpochMilli();
        callDataWebhookRepository.deleteMissCallByDate(milliseconds);
    }

}
