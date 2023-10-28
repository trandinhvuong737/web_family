package deepstream.ttrack.controller;

import deepstream.ttrack.dto.DateRangeDto;
import deepstream.ttrack.service.ExcelService;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/excel")
@AllArgsConstructor
public class ExcelController {

    private final ExcelService excelService;

    @PostMapping("/export")
    @PreAuthorize("isAuthenticated()")
    public void exportToExcel(HttpServletResponse response,
                              @RequestBody DateRangeDto dateRangeDto) throws IOException {
        excelService.exportToExcel(response, dateRangeDto);
    }

    @PostMapping("/export-vn-post")
    @PreAuthorize("isAuthenticated()")
    public void exportToExcelVnPost(HttpServletResponse response,
                                    @RequestBody DateRangeDto dateRangeDto) throws IOException {
        excelService.exportToExcelVnPost(response, dateRangeDto);
    }
}
