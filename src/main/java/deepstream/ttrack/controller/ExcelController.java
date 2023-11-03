package deepstream.ttrack.controller;

import deepstream.ttrack.common.constant.Constant;
import deepstream.ttrack.dto.DateRangeDto;
import deepstream.ttrack.dto.ResponseJson;
import deepstream.ttrack.service.ExcelService;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/excel")
@AllArgsConstructor
public class ExcelController {

    private final ExcelService excelService;

    @PostMapping("/export")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public void exportToExcel(HttpServletResponse response,
                              @RequestBody DateRangeDto dateRangeDto) throws IOException {
        excelService.exportToExcel(response, dateRangeDto);
    }

    @PostMapping("/export-vn-post")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public void exportToExcelVnPost(HttpServletResponse response,
                                    @RequestBody DateRangeDto dateRangeDto) throws IOException {
        excelService.exportToExcelVnPost(response, dateRangeDto);
    }

    @PostMapping("/import-file-excel")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ResponseJson> importFileExcel(@RequestBody List<String> listPhoneNumber){
        excelService.updateStatusByListPhone(listPhoneNumber);
        return ResponseEntity.ok().body(
                new ResponseJson<>(HttpStatus.OK, Constant.SUCCESS));
    }
}
