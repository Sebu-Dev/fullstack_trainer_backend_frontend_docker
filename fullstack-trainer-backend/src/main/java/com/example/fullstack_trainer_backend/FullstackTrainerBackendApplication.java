package com.example.fullstack_trainer_backend;

import java.io.IOException;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.fullstack_trainer_backend.utils.CsvImporter;
import com.opencsv.exceptions.CsvException;

@SpringBootApplication
public class FullstackTrainerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FullstackTrainerBackendApplication.class, args);
		Scanner scanner = new Scanner(System.in);

		// Abfrage, ob CSV eingelesen werden soll
		System.out.println("Möchten Sie die Daten aus der CSV-Datei einlesen? (y/n)");

		// Eingabe vom Benutzer
		String eingabe = scanner.nextLine().toLowerCase(); // .toLowerCase sorgt dafür, dass die Eingabe nicht
															// case-sensitive ist

		if (eingabe.equals("y")) {
			System.out.println("Daten werden eingelesen...");
			String questionCsvPath = "/Users/vwbspk0/Desktop/VsCode/python/categories.csv";
			String answerCsvPath = "/Users/vwbspk0/Desktop/VsCode/python/answers.csv";
			String categoriesCsvPath = "/Users/vwbspk0/Desktop/VsCode/python/questions.csv";

			try {
				private csvImporter=new CsvImporter();
				CsvImporter.importQuestions(questionCsvPath, answerCsvPath, categoriesCsvPath);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CsvException e) {
				e.printStackTrace();
			}

		} else if (eingabe.equals("n")) {
			System.out.println("Daten werden nicht eingelesen.");
		} else {
			System.out.println("Ungültige Eingabe. Bitte geben Sie 'y' für Ja oder 'n' für Nein ein.");
		}

		// Scanner schließen
		scanner.close();
	}

}
