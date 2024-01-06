package deepstream.ttrack.controller;

import deepstream.ttrack.common.constant.Constant;
import deepstream.ttrack.dto.DateRangeDto;
import deepstream.ttrack.dto.ResponseJson;
import deepstream.ttrack.service.ExcelService;
import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
@AllArgsConstructor
public class ExcelController {

    private final ExcelService excelService;
    private static final Logger logger = LoggerFactory.getLogger(ExcelController.class);

    @PostMapping("/export")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public void exportToExcel(HttpServletResponse response,
                              @RequestBody DateRangeDto dateRangeDto) throws IOException {
        logger.info("exportToExcel");
        excelService.exportToExcel(response, dateRangeDto);
    }

    @PostMapping("/export-vn-post")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public void exportToExcelVnPost(HttpServletResponse response,
                                    @RequestBody DateRangeDto dateRangeDto) throws IOException {
        logger.info("exportToExcelVnPost");
        excelService.exportToExcelVnPost(response, dateRangeDto);
    }

    @PostMapping("/import-file-excel")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ResponseJson<Boolean>> importFileExcel(@RequestBody List<String> listPhoneNumber){
        logger.info("importFileExcel");
        excelService.updateStatusByListPhone(listPhoneNumber);
        return ResponseEntity.ok().body(
                new ResponseJson<>(true,HttpStatus.OK, Constant.SUCCESS));
    }
}
