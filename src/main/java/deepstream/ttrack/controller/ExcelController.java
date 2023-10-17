package deepstream.ttrack.controller;

import deepstream.ttrack.service.ExcelService;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/excel")
@AllArgsConstructor
public class ExcelController {

    private final ExcelService excelService;

    @GetMapping("/export")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<byte[]> exportToExcel(HttpServletResponse response) throws IOException {
        excelService.exportToExcel(response);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.xlsx")
                .body(response.getOutputStream().toString().getBytes());
    }

    @GetMapping("/export-vn-post")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<byte[]> exportToExcelVnPost(HttpServletResponse response) throws IOException {
        excelService.exportToExcelVnPost(response);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.xlsx")
                .body(response.getOutputStream().toString().getBytes());
    }
}
