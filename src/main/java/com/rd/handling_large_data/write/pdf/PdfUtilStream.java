package com.rd.handling_large_data.write.pdf;

import com.rd.handling_large_data.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PdfUtilStream {

    private PDFont pdFont;

    private PDFont getFont(PDDocument document) throws IOException {
        if (pdFont == null) {
            pdFont = PDType0Font.load(document, getClass().getClassLoader().getResourceAsStream("font" + File.separator + "arial" + File.separator + "arial.ttf"));
        }
        return pdFont;
    }

    private PDPageContentStream formatPage(PDDocument document, PDPage page) throws IOException {

        PDPageContentStream contents = new PDPageContentStream(document, page);
        contents.setFont(getFont(document), 12);
        contents.setLeading(14.5f);

        contents.beginText();
        contents.newLineAtOffset(25, 700);

        return contents;
    }

    private PDPageContentStream createPage(PDDocument document) throws IOException {
        PDPage page = new PDPage();
        document.addPage(page);
        return formatPage(document, page);
    }

    private void writeUsingStreamApi(String fileName, List<LinkedList<String>> rows) {

        final int MAX_LINE_PER_PAGE = 50;
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();

        try {
            document.addPage(page);
            PDPageContentStream contents = formatPage(document, page);

            int linePerPage = 0;
            for (LinkedList<String> colList : rows) {
                if (linePerPage == MAX_LINE_PER_PAGE) {
                    contents.endText();
                    contents.close();
                    contents = createPage(document);
                    linePerPage = 0;
                }

                contents.showText(StringUtils.join(colList, ";"));
                contents.newLine();
                linePerPage += 1;
            }

            contents.endText();
            contents.close();

            document.save(fileName);
        } catch (Exception ex) {
            // DO SOMETHING
        } finally {
            try {
                document.close();
            } catch (IOException e) {
                // DO SOMETHING
            }
        }
    }

    public static void write(Data d) {
        PdfUtilStream app = new PdfUtilStream();

        System.out.print("+ Write " + d.getLabel() + " .PDF rows consume : ");
        long startTime = System.currentTimeMillis();

        String fileName = StringUtils.join("out", File.separator, "PdfUtilStream", d.getLabel(), ".pdf");
        app.writeUsingStreamApi(fileName, d.getRows());

        long endTime = System.currentTimeMillis() - startTime;
        System.out.print(TimeUnit.MILLISECONDS.toSeconds(endTime) + " seconds");
        System.out.println();
    }
}
