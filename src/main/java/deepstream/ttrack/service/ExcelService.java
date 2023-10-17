package deepstream.ttrack.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ExcelService {

    void exportToExcel(HttpServletResponse response) throws IOException;

    void exportToExcelVnPost(HttpServletResponse response) throws IOException;
}
