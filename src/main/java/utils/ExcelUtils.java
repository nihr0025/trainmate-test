package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ExcelUtils for reading Excel test data files (.xlsx) dynamically using Apache POI.
 */
public class ExcelUtils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Reads all data rows from a sheet as a 2D Object array suitable for TestNG DataProvider.
     * Excludes header row.
     */
    public static Object[][] getSheetData(String filePath, String sheetName) {
        List<Map<String, String>> rowsList = getSheetDataAsMap(filePath, sheetName);
        Object[][] data = new Object[rowsList.size()][1];
        for (int i = 0; i < rowsList.size(); i++) {
            data[i][0] = rowsList.get(i);
        }
        return data;
    }

    /**
     * Reads sheet data into a List of Maps (Header Name -> Cell Value).
     */
    public static List<Map<String, String>> getSheetDataAsMap(String filePath, String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            LoggerUtils.info(ExcelUtils.class, "Excel file does not exist at " + file.getAbsolutePath() + ". Generating from specification template...");
            try {
                ExcelDataInitializer.main(new String[0]);
            } catch (Exception e) {
                LoggerUtils.warn(ExcelUtils.class, "Could not auto-generate testdata.xlsx: " + e.getMessage());
            }
        }

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                LoggerUtils.warn(ExcelUtils.class, "Sheet '" + sheetName + "' not found in workbook: " + filePath);
                return dataList;
            }

            int totalRows = sheet.getLastRowNum();
            if (totalRows < 1) {
                return dataList; // No data rows
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                return dataList;
            }

            int totalCols = headerRow.getLastCellNum();
            List<String> headers = new ArrayList<>();
            for (int col = 0; col < totalCols; col++) {
                Cell cell = headerRow.getCell(col);
                headers.add(getCellValueAsString(cell).trim());
            }

            DataFormatter formatter = new DataFormatter();
            for (int r = 1; r <= totalRows; r++) {
                Row row = sheet.getRow(r);
                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                Map<String, String> rowMap = new LinkedHashMap<>();
                for (int col = 0; col < totalCols; col++) {
                    String header = headers.get(col);
                    if (header.isEmpty()) {
                        continue;
                    }
                    Cell cell = row.getCell(col);
                    String val = getCellValueAsString(cell);
                    rowMap.put(header, val);
                }
                dataList.add(rowMap);
            }

        } catch (IOException e) {
            LoggerUtils.error(ExcelUtils.class, "Error reading Excel file: " + e.getMessage(), e);
        }

        return dataList;
    }

    /**
     * Reads sheet rows as a 2D Object array of String columns (row x column).
     */
    public static Object[][] getSheetDataAsArray(String filePath, String sheetName) {
        List<Map<String, String>> maps = getSheetDataAsMap(filePath, sheetName);
        if (maps.isEmpty()) {
            return new Object[0][0];
        }

        int colCount = maps.get(0).size();
        Object[][] result = new Object[maps.size()][colCount];

        for (int i = 0; i < maps.size(); i++) {
            Map<String, String> row = maps.get(i);
            int j = 0;
            for (String val : row.values()) {
                result[i][j++] = val;
            }
        }
        return result;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        CellType cellType = cell.getCellType();
        if (cellType == CellType.FORMULA) {
            cellType = cell.getCachedFormulaResultType();
        }

        switch (cellType) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    return date != null ? DATE_FORMAT.format(date) : "";
                } else {
                    double num = cell.getNumericCellValue();
                    if (num == Math.floor(num) && !Double.isInfinite(num)) {
                        return String.valueOf((long) num);
                    } else {
                        return String.valueOf(num);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    private static boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK && !getCellValueAsString(cell).trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
