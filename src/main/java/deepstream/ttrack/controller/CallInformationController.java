package deepstream.ttrack.controller;

import deepstream.ttrack.common.constant.Constant;
import deepstream.ttrack.dto.ResponseJson;
import deepstream.ttrack.dto.calldatawebhook.CallDTO;

import deepstream.ttrack.entity.CallHistory;
import deepstream.ttrack.service.CallDataWebhookService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/call-information")
@AllArgsConstructor
public class CallInformationController {

    private final CallDataWebhookService callDataWebhookService;
    private static final Logger logger = LoggerFactory.getLogger(CallInformationController.class);

    @PostMapping("/")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ResponseJson<Boolean>> addCallInformation(@RequestBody CallDTO callDTO) {
        logger.info("addCallInformation");
        callDataWebhookService.addCallInformation(callDTO);
        return ResponseEntity.ok().body(
                new ResponseJson<>(true, HttpStatus.OK, Constant.SUCCESS));

    }

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<List<CallHistory>>> getAllMissCall() {
        logger.info("getAllMissCall");
        List<CallHistory> callHistories = callDataWebhookService.getAllMissCall();
        return ResponseEntity.ok().body(
                new ResponseJson<>(callHistories, HttpStatus.OK, Constant.SUCCESS));

    }

    @GetMapping("/total-miss-call")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<Integer>> totalMissCall() {
        logger.info("totalMissCall");
        Integer totalCall = callDataWebhookService.totalMissCall();
        return ResponseEntity.ok().body(
                new ResponseJson<>(totalCall, HttpStatus.OK, Constant.SUCCESS));

    }
}
