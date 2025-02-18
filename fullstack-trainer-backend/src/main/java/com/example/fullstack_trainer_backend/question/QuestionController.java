package com.example.fullstack_trainer_backend.question;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import com.example.fullstack_trainer_backend.question.dtos.QuestionDto;
@RestController
@RequestMapping("/questions")
@CrossOrigin(origins = "http://localhost:3000")
public class QuestionController {

    private final QuestionService questionService;
    private final ObjectMapper objectMapper;

    

    public QuestionController(QuestionService questionService, ObjectMapper objectMapper) {
        this.questionService = questionService;
        this.objectMapper = objectMapper;
    }
    @GetMapping
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestionById(id));
    }

    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody QuestionDto questionDto) {
        Question createdQuestion = questionService.createQuestion(questionDto);
        return ResponseEntity.ok(createdQuestion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @RequestBody QuestionDto questionDto) {
        Question updatedQuestion = questionService.updateQuestion(id, questionDto);
        return ResponseEntity.ok(updatedQuestion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok("Frage gelöscht");
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> bulkCreateQuestions(@RequestBody List<QuestionDto> questionsDTO) {
        questionsDTO.forEach(System.out::println);
        try {
            List<Question> questions = questionsDTO.stream()
                    .map(questionService::createQuestion)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fehler bei der Anfrage: " + e.getMessage());
        }
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadQuestions(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Die Datei ist leer");
        }
    
        try {
            // Fragen aus der Datei lesen
            List<Question> questions = objectMapper.readValue(file.getInputStream(), new TypeReference<>() {});
    
            // Fragen speichern und Fehlerbehandlung durchführen
            SaveResult saveResult = questionService.saveAll(questions);
    
            // Erfolgreich gespeicherte Fragen und fehlgeschlagene Fragen extrahieren
            List<Question> successfullySavedQuestions = saveResult.getSavedQuestions();
            List<String> failedQuestionsText = saveResult.getFailedQuestionsText();
    
            int failedCount = failedQuestionsText.size();
            
            // Rückgabemeldung je nach Erfolg oder teilweise fehlerhaftem Speichern
            if (failedCount > 0) {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(
                    "Einige Fragen konnten nicht gespeichert werden. Erfolgreich gespeichert: " +
                    successfullySavedQuestions.size() + ", Fehlgeschlagen: " + failedCount + 
                    ". Fehlende Fragen: " + String.join(", ", failedQuestionsText));
            } else {
                return ResponseEntity.ok("Fragen erfolgreich gespeichert: " + successfullySavedQuestions.size());
            }
    
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Fehler beim Verarbeiten der Datei: " + e.getMessage());
        }
    }
    
}
