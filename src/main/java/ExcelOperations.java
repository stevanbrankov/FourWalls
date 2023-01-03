import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExcelOperations {

    private ExcelOperations() {
    }

    private static String dbLocation = System.getProperty("user.dir") + "\\src\\main\\resources\\FourWalls.xlsx";

    public static void writeInExcelFile(Set<String> links) {
        try (FileInputStream fis = new FileInputStream(dbLocation)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (String link : links) {
                int rowCount = sheet.getLastRowNum();

                XSSFRow row = sheet.createRow(++rowCount);
                int columnCount = 0;
                XSSFCell cell = row.createCell(columnCount++);
                cell.setCellValue(link);
            }

            FileOutputStream outputStream = new FileOutputStream(dbLocation);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<String> readFromExcelFile() throws IOException {
        List<String> data = new ArrayList<>();
        try (FileInputStream file = new FileInputStream(dbLocation);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                for (Cell cell : row) {
                    data.add(cell.getStringCellValue());
                }
            }
        }
        return data;
    }
}
