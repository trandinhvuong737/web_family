package deepstream.ttrack.service;

import deepstream.ttrack.dto.DateRangeDto;
import deepstream.ttrack.entity.Order;
import deepstream.ttrack.entity.Product;
import deepstream.ttrack.repository.OrderRepository;
import deepstream.ttrack.repository.ProductRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelServiceIml implements ExcelService{

    public static final String ASIA_HO_CHI_MINH = "Asia/Ho_Chi_Minh";
    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    public ExcelServiceIml(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }


    @Override
    public void exportToExcel(HttpServletResponse response, DateRangeDto dateRangeDto) throws IOException {

        ZoneId zoneId = ZoneId.of(ASIA_HO_CHI_MINH);
        // Create a ZonedDateTime with the LocalDate and time zone
        LocalDate startDate = dateRangeDto.getStartDate().atStartOfDay(zoneId).toLocalDate();
        LocalDate endDate = dateRangeDto.getStartDate().atStartOfDay(zoneId).toLocalDate();

        List<Order> orders = orderRepository.getOrdersByStatus(startDate,endDate);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Tên Khách hàng");
        headerRow.createCell(1).setCellValue("Địa Chỉ");
        headerRow.createCell(2).setCellValue("Số Điện Thoại");
        headerRow.createCell(3).setCellValue("Tên Sản Phẩm");
        headerRow.createCell(4).setCellValue("Số Lượng");
        headerRow.createCell(5).setCellValue("Khối Lượng(gram)");
        headerRow.createCell(6).setCellValue("LCOD");
        int i = 1;
        for (Order order:orders
             ) {
            Product product = productRepository.getProductByProductName(order.getProduct());

            Row dataRow = sheet.createRow(i);
            dataRow.createCell(0).setCellValue(order.getCustomer());
            dataRow.createCell(1).setCellValue(order.getAddress());
            dataRow.createCell(2).setCellValue(order.getPhoneNumber());
            dataRow.createCell(3).setCellValue(order.getProduct());
            dataRow.createCell(4).setCellValue(order.getQuantity());
            dataRow.createCell(5).setCellValue(product.getWeight());
            dataRow.createCell(6).setCellValue(product.getWeight() * 2000 * order.getQuantity());
            i+=1;
        }

        for (int j=0; j<=6; j++){
            headerStyle(workbook, headerRow, j);
            sheet.autoSizeColumn(j);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=data.xlsx");

        workbook.write(response.getOutputStream());
        response.flushBuffer();
    }

    @Override
    public void exportToExcelVnPost(HttpServletResponse response, DateRangeDto dateRangeDto) throws IOException {

        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        // Create a ZonedDateTime with the LocalDate and time zone
        LocalDate startDate = dateRangeDto.getStartDate().atStartOfDay(zoneId).toLocalDate();
        LocalDate endDate = dateRangeDto.getStartDate().atStartOfDay(zoneId).toLocalDate();

        List<Order> orders = orderRepository.getOrdersByStatus(startDate,endDate);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("STT");
        headerRow.createCell(1).setCellValue("TÊN NGƯỜI NHẬN (*)");
        headerRow.createCell(2).setCellValue("ĐIỆN THOẠI NHẬN (*)");
        headerRow.createCell(3).setCellValue("ĐỊA CHỈ ĐẦY ĐỦ (*)");
        headerRow.createCell(4).setCellValue("DỊCH VỤ (*)");
        headerRow.createCell(5).setCellValue("CHO XEM HÀNG");
        headerRow.createCell(6).setCellValue("HÌNH THỨC THU GOM (*)");
        headerRow.createCell(7).setCellValue("KHỐI LƯỢNG (GAM) (*)");
        headerRow.createCell(8).setCellValue("CHỈ DẪN PHÁT");
        headerRow.createCell(9).setCellValue("NỘI DUNG");
        headerRow.createCell(10).setCellValue("TIỀN THU HỘ - COD (VNĐ)");

        int i = 1;
        for (Order order:orders
        ) {
            Product product = productRepository.getProductByProductName(order.getProduct());

            Row dataRow = sheet.createRow(i);
            dataRow.createCell(0).setCellValue(i);
            dataRow.createCell(1).setCellValue(order.getCustomer());
            dataRow.createCell(2).setCellValue(order.getPhoneNumber());
            dataRow.createCell(3).setCellValue(order.getAddress());
            dataRow.createCell(4).setCellValue("TMĐT - Chuyển phát tiết kiệm (32)");
            dataRow.createCell(5).setCellValue("X");
            dataRow.createCell(6).setCellValue("Thu gom tận nơi (1)");
            dataRow.createCell(7).setCellValue(order.getQuantity());
            dataRow.createCell(8).setCellValue("Cho xem hàng.có vde gọi hotline:086561027");
            dataRow.createCell(9).setCellValue(order.getProduct());
            dataRow.createCell(10).setCellValue(product.getWeight() * 2000 * order.getQuantity());
            i+=1;
        }

        for (int j=0; j<=10; j++){
            headerStyle(workbook, headerRow, j);
            sheet.autoSizeColumn(j);
        }

        LocalDate date = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyy");
        String formattedDate = date.format(formatter);
        String filename = "attachment; filename=data".concat(formattedDate).concat(".xlsx");

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", filename);

        workbook.write(response.getOutputStream());
        response.flushBuffer();
    }

    private static void headerStyle(Workbook workbook, Row headerRow, int index) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setColor(IndexedColors.WHITE.getIndex()); // Set font color to white
        headerFont.setBold(true); // Make the font bold
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex()); // Set background color
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerRow.getCell(index).setCellStyle(headerStyle);
    }
}
