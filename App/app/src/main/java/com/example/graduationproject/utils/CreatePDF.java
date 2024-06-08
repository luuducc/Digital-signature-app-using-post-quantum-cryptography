package com.example.graduationproject.utils;

import android.content.Context;

import com.example.graduationproject.config.MyConstant;
import com.example.graduationproject.data.remote.Transcript;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class CreatePDF {
    public static void createPdf(List<Transcript.StudentGrade> studentGradeList, String className) throws IOException {
        String pdfFolderPath = MyConstant.GRADUATION_PROJECT_FOLDER + "/Transcripts";
        File customFolder = new File(pdfFolderPath);
        // create pdf folder if not exist
        if (!customFolder.exists()) {
            customFolder.mkdirs();
        }

        // create file to store pdf transcript
        File file = new File(pdfFolderPath,className + ".pdf");
        if(file.exists()) {
            file.delete();
        }
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Add heading
        Paragraph heading = new Paragraph("Ha Noi University of Science and Technology")
                .setFontColor(ColorConstants.BLACK)
                .setBold()
                .setFontSize(15)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(heading);

        // Add class information
        Paragraph classInfo = new Paragraph("Majors: IT2\nClass: " + className)
                .setFontColor(ColorConstants.BLACK)
                .setFontSize(12);
        document.add(classInfo);

        Table table = new Table(new float[]{50f, 200f, 200f});

        // Add table header
        table.addCell("Index");
        table.addCell("Name");
        table.addCell("Grade");

        // Add rows to the table
        for (int i = 0; i < studentGradeList.size(); i++) {
            table.addCell(String.valueOf(i + 1));
            table.addCell(studentGradeList.get(i).getName());
            table.addCell(String.valueOf(studentGradeList.get(i).getGrade()));
        }
        document.add(table);

        // Add footer
        Paragraph footer = new Paragraph("Bui Trong Tung")
                .setFontColor(ColorConstants.BLACK)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.RIGHT);
        document.add(footer);

        document.close();
        outputStream.close();
    }


}


