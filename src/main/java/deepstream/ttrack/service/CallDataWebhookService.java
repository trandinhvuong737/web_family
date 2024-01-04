package deepstream.ttrack.service;

import deepstream.ttrack.dto.calldatawebhook.CallDTO;
import deepstream.ttrack.entity.CallHistory;

import java.util.List;

public interface CallDataWebhookService {
    void addCallInformation(CallDTO callDTO);

    List<CallHistory> getAllMissCall();
}
