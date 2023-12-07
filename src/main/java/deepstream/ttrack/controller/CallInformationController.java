package deepstream.ttrack.controller;

import deepstream.ttrack.common.constant.Constant;
import deepstream.ttrack.dto.ResponseJson;
import deepstream.ttrack.dto.calldatawebhook.CallDTO;

import deepstream.ttrack.entity.MissedCall;
import deepstream.ttrack.service.CallDataWebhookService;
import lombok.AllArgsConstructor;
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
    @PostMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ResponseJson<Boolean>> addCallInformation(@PathVariable String id,
                                                              @RequestBody CallDTO callDTO) {
        callDataWebhookService.addCallInformation(id, callDTO);
        return ResponseEntity.ok().body(
                new ResponseJson<>(true, HttpStatus.OK, Constant.SUCCESS));

    }

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<List<MissedCall>>> getAllMissCall() {
        List<MissedCall> missedCalls = callDataWebhookService.getAllMissCall();
        return ResponseEntity.ok().body(
                new ResponseJson<>(missedCalls, HttpStatus.OK, Constant.SUCCESS));

    }
}
