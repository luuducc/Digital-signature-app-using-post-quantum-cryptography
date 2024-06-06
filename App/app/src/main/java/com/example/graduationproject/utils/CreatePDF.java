package com.example.graduationproject.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.graduationproject.TranscriptData;
import com.example.graduationproject.data.remote.Transcript;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class CreatePDF {
    public static void createPdf(Context context, List<Transcript.StudentGrade> studentGradeList, String className) throws IOException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath,className + ".pdf");
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


