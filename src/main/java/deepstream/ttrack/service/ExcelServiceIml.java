package deepstream.ttrack.service;

import deepstream.ttrack.entity.Order;
import deepstream.ttrack.entity.Product;
import deepstream.ttrack.repository.OrderRepository;
import deepstream.ttrack.repository.ProductRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelServiceIml implements ExcelService{

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    public ExcelServiceIml(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }


    @Override
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<Order> orders = orderRepository.getOrdersByStatus();
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

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);


        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=data.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
