package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CreatePDF {

    private final Map<String, String> subjectTeacherMap = new HashMap<>();

    public void createSchedulePDF(Context context) {
        try {
            populateSubjectTeacherMap();

            // Définir le chemin du fichier pour le PDF (par exemple, dans le stockage externe)
            String filePath = context.getExternalFilesDir(null) + "/schedule.pdf";

            // Étape 1 : Créer une instance de PdfWriter pour iText 7
            PdfWriter writer = new PdfWriter(filePath);

            // Étape 2 : Créer une instance de PdfDocument
            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf);

            // Ajouter le titre
            document.add(new Paragraph("Informatics Timetable"));
            document.add(new Paragraph("--------------------------------------------------"));

            // Définir les largeurs des colonnes du tableau
            float[] columnWidths = {1, 1, 1, 1, 1, 1};
            Table table = new Table(columnWidths);

            table.addCell("Time");
            table.addCell("Monday");
            table.addCell("Tuesday");
            table.addCell("Wednesday");
            table.addCell("Thursday");
            table.addCell("Friday");

            // Ajouter les lignes du programme avec les matières et les noms des enseignants
            addOrganizedScheduleRow(table, "08:00-09:00", "Algorithms", "Networking", "Algorithms", "Networking", "Algorithms");
            addOrganizedScheduleRow(table, "09:00-10:00", "Data Structures", "Databases", "Data Structures", "Databases", "Data Structures");
            addOrganizedScheduleRow(table, "10:00-11:00", "Break", "Break", "Break", "Break", "Break");
            addOrganizedScheduleRow(table, "11:00-12:00", "Operating Systems", "Programming", "Operating Systems", "Programming", "Operating Systems");
            addOrganizedScheduleRow(table, "12:00-13:00", "Lunch Break", "Lunch Break", "Lunch Break", "Lunch Break", "Lunch Break");
            addOrganizedScheduleRow(table, "13:00-14:00", "Machine Learning", "Web Development", "Machine Learning", "Web Development", "Machine Learning");
            addOrganizedScheduleRow(table, "14:00-15:00", "Cybersecurity", "Software Engineering", "Cybersecurity", "Software Engineering", "Cybersecurity");


            document.add(table);


            document.close();

            // Informer l'utilisateur de la création réussie
            Toast.makeText(context, "PDF Created Successfully at: " + filePath, Toast.LENGTH_LONG).show();

            // Optionnellement, ouvrir le PDF généré
            openPDF(context, filePath);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error creating PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateSubjectTeacherMap() {
        subjectTeacherMap.put("Algorithms", "Mme Wissal Frihi");
        subjectTeacherMap.put("Networking", "Mme Mariem Ben Miled");
        subjectTeacherMap.put("Data Structures", "Mme Afef Ben Slama");
        subjectTeacherMap.put("Databases", "Mme Afef Ben Slama");
        subjectTeacherMap.put("Operating Systems", "Mr Houcine Essid");
        subjectTeacherMap.put("Programming", "Mr Houcine Essid");
        subjectTeacherMap.put("Machine Learning", "Mme Nada");
        subjectTeacherMap.put("Web Development", "Mme Dhekra");
        subjectTeacherMap.put("Cybersecurity", "Mme Wided Guezguez");
        subjectTeacherMap.put("Software Engineering", "Mme Raouia Ayachi");
    }

    private void addOrganizedScheduleRow(Table table, String time, String monday, String tuesday, String wednesday, String thursday, String friday) {
        table.addCell("Time: " + time);


        table.addCell(getDisplayForBreakOrLunch(monday));
        table.addCell(getDisplayForBreakOrLunch(tuesday));
        table.addCell(getDisplayForBreakOrLunch(wednesday));
        table.addCell(getDisplayForBreakOrLunch(thursday));
        table.addCell(getDisplayForBreakOrLunch(friday));
    }

    private String getDisplayForBreakOrLunch(String subject) {

        if ("Break".equals(subject) || "Lunch Break".equals(subject)) {
            return subject;
        }

        return "Subject: " + subject + "\nTeacher: " + getTeacherName(subject);
    }

    private String getTeacherName(String subject) {
        return subjectTeacherMap.getOrDefault(subject, "Unknown");
    }

    private void openPDF(Context context, String filePath) {
        File pdfFile = new File(filePath);
        Uri pdfUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", pdfFile);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(intent);
    }
}
