package com.giko.ems.exporter;

import com.giko.ems.model.Employee;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class EmployeeExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Employee> listEmployees;

    public EmployeeExcelExporter(List<Employee> listEmployees){
        this.listEmployees = listEmployees;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine(){
        sheet = workbook.createSheet("Employees");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "First Name", style);
        createCell(row, 1, "Last Name", style);
        createCell(row, 2, "Email", style);
        createCell(row, 3, "Department", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style){
        sheet.autoSizeColumn(columnCount);

        Cell cell = row.createCell(columnCount);

        if (value instanceof Integer){
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean){
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines(){
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Employee employee : listEmployees){
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, employee.getFirstName(), style);
            createCell(row, columnCount++, employee.getLastName(), style);
            createCell(row, columnCount++, employee.getEmail(), style);
            createCell(row, columnCount++, employee.getDepartment(), style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
}
