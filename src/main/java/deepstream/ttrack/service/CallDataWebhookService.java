package deepstream.ttrack.service;

import deepstream.ttrack.dto.calldatawebhook.CallDTO;
import deepstream.ttrack.entity.MissedCall;

import java.util.List;

public interface CallDataWebhookService {
    void addCallInformation(String id, CallDTO callDTO);

    List<MissedCall> getAllMissCall();
}
