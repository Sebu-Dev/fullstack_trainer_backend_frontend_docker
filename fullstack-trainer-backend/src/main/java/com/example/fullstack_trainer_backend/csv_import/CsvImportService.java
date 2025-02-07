package com.example.fullstack_trainer_backend.csv_import;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.exceptions.CsvException;

@Service
public class CsvImportService {

    @Autowired
    private CsvImporter csvImporter;

    public void importCsvData() {
        // Die Pfade zu den CSV-Dateien
        String questionCsvPath = "/Users/vwbspk0/Desktop/VsCode/python/categories.csv";
        String answerCsvPath = "/Users/vwbspk0/Desktop/VsCode/python/answers.csv";
        String categoriesCsvPath = "/Users/vwbspk0/Desktop/VsCode/python/questions.csv";

        try {
            csvImporter.importQuestions(questionCsvPath, answerCsvPath, categoriesCsvPath);
            System.out.println("Daten wurden erfolgreich eingelesen.");
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Einlesen der CSV-Dateien.");
        }
    }
}