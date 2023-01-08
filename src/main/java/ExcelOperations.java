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

public class ExcelOperations {

    private ExcelOperations() {
    }

    private static String dbLocation = System.getProperty("user.dir") + "\\src\\main\\resources\\FourWalls.xlsx";

    public static void writeNewDataInExcelFile(List<List<String>> insertData) {
        try (FileInputStream fis = new FileInputStream(dbLocation)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (List<String> oneApartmentData : insertData) {
                int rowCount = sheet.getLastRowNum();

                XSSFRow row = sheet.createRow(++rowCount);
                XSSFCell cellLink = row.createCell(0);
                XSSFCell cellPrice = row.createCell(1);
                cellLink.setCellValue(oneApartmentData.get(0));
                cellPrice.setCellValue(oneApartmentData.get(1));
            }

            FileOutputStream outputStream = new FileOutputStream(dbLocation);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeExistingDataInExcelFile(List<List<String>> insertData) {
        try (FileInputStream fis = new FileInputStream(dbLocation)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet){
                String linkFromExcel = row.getCell(0).getStringCellValue();
                for (List<String> oneApartmentData : insertData) {
                    String linkFromInsertData = oneApartmentData.get(0);
                    if (linkFromExcel.equals(linkFromInsertData)){
                        XSSFRow xssfRow = sheet.createRow(row.getRowNum());
                        XSSFCell cellLink = xssfRow.createCell(0);
                        cellLink.setCellValue(oneApartmentData.get(0));
                        XSSFCell cellPrice = xssfRow.createCell(1);
                        cellPrice.setCellValue(oneApartmentData.get(1));
                    }
                }
            }
            FileOutputStream outputStream = new FileOutputStream(dbLocation);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<List<String>> readFromExcelFile() throws IOException {
        List<List<String>> excelData = new ArrayList<>();
        try (FileInputStream file = new FileInputStream(dbLocation); Workbook workbook = new XSSFWorkbook(file)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                List<String> rowList = new ArrayList<>();
                for (Cell cell : row) {
                    rowList.add(cell.getStringCellValue());
                }
                excelData.add(rowList);
            }
        }
        return excelData;
    }
}
