package com.example.fullstack_trainer_backend.csv_import;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.fullstack_trainer_backend.question.DifficultyEnum;
import com.example.fullstack_trainer_backend.question.Question;
import com.example.fullstack_trainer_backend.question.QuestionRepository;
import com.example.fullstack_trainer_backend.question.answer.Answer;
import com.example.fullstack_trainer_backend.question.answer.AnswerRepository;
import com.example.fullstack_trainer_backend.question.category.Category;
import com.example.fullstack_trainer_backend.question.category.CategoryRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

@Component
public class CsvImporter {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public void importQuestions(String questionCsvPath, String answerCsvPath, String categoriesCsvPath)
            throws IOException, CsvException {

        List<Question> questions = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();
        List<Category> categories = new ArrayList<>();

        // Einlesen der Fragen (question.csv)
        try (CSVReader reader = new CSVReader(new FileReader(questionCsvPath))) {
            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                if (record[0].equals("QuestionID"))
                    continue; // Skip header
                Question question = new Question();
                question.setTopic(record[1]);
                question.setText(record[2]);
                question.setDifficulty(DifficultyEnum.valueOf(record[3].toUpperCase()));
                question.setExplanation(record[4]);
                question.setImageUrl(record[5]);
                question.setMaxPoints(Integer.parseInt(record[6]));
                questions.add(question);
            }
        }

        // Einlesen der Antworten (answer.csv)
        try (CSVReader reader = new CSVReader(new FileReader(answerCsvPath))) {
            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                if (record[0].equals("AnswerID"))
                    continue; // Skip header
                Answer answer = new Answer();
                answer.setId(Long.parseLong(record[0]));
                answer.setText(record[2]);
                answer.setIs_correct(Boolean.parseBoolean(record[3]));
                answers.add(answer);
            }
        }

        // Einlesen der Kategorien (categories.csv)
        try (CSVReader reader = new CSVReader(new FileReader(categoriesCsvPath))) {
            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                if (record[0].equals("CategoryID"))
                    continue; // Skip header
                Category category = new Category();
                category.setId(Long.parseLong(record[0]));
                category.setCategory(record[2]);
                categories.add(category);
            }
        }

        // Nun die Daten in die Datenbank speichern
        saveQuestionsAndLink(questions, answers, categories);
    }

    private void saveQuestionsAndLink(List<Question> questions, List<Answer> answers, List<Category> categories) {
        // Speichern der Kategorien
        categoryRepository.saveAll(categories);

        // Speichern der Fragen und Verknüpfen mit den Kategorien
        for (Question question : questions) {
            question = questionRepository.save(question);
            question.setCategory(categories); // Verknüpfung herstellen
        }

        // Antworten zu den jeweiligen Fragen zuordnen und speichern
        for (Answer answer : answers) {
            answer.setQuestion(findQuestionById(questions, UUID.fromString(answer.getQuestion().getId().toString())));
            answerRepository.save(answer);
        }
    }

    private Question findQuestionById(List<Question> questions, UUID questionId) {
        return questions.stream()
                .filter(q -> q.getId().equals(questionId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Question not found: " + questionId));
    }
}
