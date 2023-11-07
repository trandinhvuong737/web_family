package deepstream.ttrack.service;

import deepstream.ttrack.dto.DateRangeDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ExcelService {

    void exportToExcel(HttpServletResponse response, DateRangeDto dateRangeDto) throws IOException;

    void exportToExcelVnPost(HttpServletResponse response, DateRangeDto dateRangeDto) throws IOException;

    void updateStatusByListPhone(List<String> listPhoneNumber);
}
