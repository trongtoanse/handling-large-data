package com.rd.handling_large_data;

import com.rd.handling_large_data.write.csv.CsvUtilStream;
import com.rd.handling_large_data.write.excel.ExcelUtilStream;
import com.rd.handling_large_data.write.pdf.PdfUtilStream;
import com.rd.handling_large_data.write.txt.TxtUtilStream;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class HandlingLargeData implements CommandLineRunner {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(HandlingLargeData.class, args);
        context.close();
    }

    @Override
    public void run(String... args) {

        //############################### Prepare Data ###############################
        System.out.println("\n#####  Prepare Data  #####");

        List<Data> dataList = new ArrayList<>();
        Data d50k = new Data("50k", 50000, new ArrayList<>());
        Data d120k = new Data("120k", 120000, new ArrayList<>());
        Data d500k = new Data("500k", 500000, new ArrayList<>());
        Data d1m = new Data("1m", 1000000, new ArrayList<>());

//        dataList.add(d50k);
//        dataList.add(d120k);
        dataList.add(d500k);
//        dataList.add(d1m);

        long startTime = System.currentTimeMillis();
        dataList.forEach(d -> {

            System.out.print("+ Prepare " + d.getLabel() + " rows consume : ");
            long prepareStartTime = System.currentTimeMillis();

            List<LinkedList<String>> rows = new ArrayList<>();
            int index = 1;
            for (int i = 0; i < d.getRowSize(); i++) {
                LinkedList<String> row = new LinkedList<>();
                row.add(String.valueOf(index++));
                for (int j = 1; j < 10; j++) {
                    row.add(RandomStringUtils.randomAlphabetic(0, 50));
                }
                rows.add(row);
            }
            d.setRows(rows);

            long prepareEndTime = System.currentTimeMillis() - prepareStartTime;
            System.out.print(TimeUnit.MILLISECONDS.toSeconds(prepareEndTime) + " seconds");
            System.out.println();
        });
        long endTime = System.currentTimeMillis() - startTime;
        System.out.println("====> Total : " + TimeUnit.MILLISECONDS.toSeconds(endTime) + " seconds");
        System.out.println();

        //### cmd ####
        System.out.print("Do you want to write file (y/n) : ");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        if (StringUtils.isBlank(line) || !"y".equalsIgnoreCase(line)) {
            System.exit(1);
        }

        //############################### Write Data ###############################
        System.out.println("\n#####  Write Data  #####");

        startTime = System.currentTimeMillis();
        dataList.forEach(d -> {
            ExcelUtilStream.write(d);
            CsvUtilStream.write(d);
            TxtUtilStream.write(d);
            PdfUtilStream.write(d);
            d.setRows(new ArrayList<>());
        });

        endTime = System.currentTimeMillis() - startTime;
        System.out.println("====> Total : " + TimeUnit.MILLISECONDS.toSeconds(endTime) + " seconds");
        System.out.println();

        //### cmd ####
        System.out.print("End process (y/n) : ");
        scanner = new Scanner(System.in);
        line = scanner.nextLine();
        if (StringUtils.isBlank(line) || !"y".equalsIgnoreCase(line)) {
            System.exit(1);
        }
    }
}
