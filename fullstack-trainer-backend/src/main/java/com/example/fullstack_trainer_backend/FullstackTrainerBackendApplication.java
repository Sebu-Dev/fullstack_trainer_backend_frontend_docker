package com.example.fullstack_trainer_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.fullstack_trainer_backend.csv_import.CsvImportService;

@SpringBootApplication
public class FullstackTrainerBackendApplication implements CommandLineRunner {

	@Autowired
	private CsvImportService csvImportService;

	public static void main(String[] args) {
		SpringApplication.run(FullstackTrainerBackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Starte den CSV-Import...");
		csvImportService.importCsvData();
		System.out.println("CSV-Import abgeschlossen.");
	}
}
