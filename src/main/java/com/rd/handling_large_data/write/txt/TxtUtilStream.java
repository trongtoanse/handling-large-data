package com.rd.handling_large_data.write.txt;

import com.rd.handling_large_data.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TxtUtilStream {

    private void writeUsingStreamApi(String fileName, List<LinkedList<String>> rows) {

        BufferedWriter bw = null;
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(fileName);
            bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (LinkedList<String> colList : rows) {
                bw.write(StringUtils.join(colList, ";"));
                bw.newLine();
            }
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
                if (bw != null) {
                    bw.flush();
                    bw.close();
                }
            } catch (IOException e) {
                // DO SOMETHING
            }
        }
    }

    public static void write(Data d) {
        TxtUtilStream app = new TxtUtilStream();

        System.out.print("+ Write " + d.getLabel() + " .TXT rows consume : ");
        long startTime = System.currentTimeMillis();

        String fileName = StringUtils.join("out", File.separator, "CsvUtilStream", d.getLabel(), ".txt");
        app.writeUsingStreamApi(fileName, d.getRows());

        long endTime = System.currentTimeMillis() - startTime;
        System.out.print(TimeUnit.MILLISECONDS.toSeconds(endTime) + " seconds");
        System.out.println();
    }
}
