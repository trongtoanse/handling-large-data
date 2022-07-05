package com.rd.handling_large_data.write.excel;

import com.rd.handling_large_data.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.apache.poi.ss.util.CellUtil.createCell;

public class ExcelUtilStream {

    private CellStyle setExcelBodyCellStyle(SXSSFWorkbook wb) {
        return wb.createCellStyle();
    }

    private void writeUsingStreamApi(String fileName, List<LinkedList<String>> rows) {

        // keep 100 rows in memory, exceeding rows will be flushed to disk
        SXSSFWorkbook wb = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE);
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(fileName);
            Sheet sheet = wb.createSheet();
            CellStyle cellStyle = setExcelBodyCellStyle(wb);

            int rowNum = 0;
            for (LinkedList<String> colList : rows) {
                Row row = sheet.createRow(rowNum);
                for (int cellNo = 0; cellNo < colList.size(); cellNo++) {
                    createCell(row, cellNo, colList.get(cellNo), cellStyle);
                }
                rowNum++;
            }

            wb.write(fos);
        } catch (Exception ex) {
            // DO SOMETHING
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                // DO SOMETHING
            }
            try {
                wb.dispose();
                wb.close();
            } catch (IOException e) {
                // DO SOMETHING
            }
        }
    }

    public static void write(Data d) {
        ExcelUtilStream app = new ExcelUtilStream();

        System.out.print("+ Write " + d.getLabel() + " .XLSX rows consume : ");
        long startTime = System.currentTimeMillis();

        String fileName = StringUtils.join("out", File.separator, "ExcelUtilStream", d.getLabel(), ".xlsx");
        app.writeUsingStreamApi(fileName, d.getRows());

        long endTime = System.currentTimeMillis() - startTime;
        System.out.print(TimeUnit.MILLISECONDS.toSeconds(endTime) + " seconds");
        System.out.println();
    }
}
