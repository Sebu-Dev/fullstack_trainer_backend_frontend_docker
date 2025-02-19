package com.example.fullstack_trainer_backend.question;

import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.fullstack_trainer_backend.question.dtos.QuestionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<List<QuestionDto>> getAllQuestions() {
        List<QuestionDto> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody QuestionDto questionDto) {
        try {
            // Der Service kümmert sich intern um Existenzprüfungen.
            Question created = questionService.createQuestion(questionDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @RequestBody QuestionDto questionDto) {
        return questionService.updateQuestion(id, questionDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long id) {
        if (questionService.deleteQuestion(id)) {
            return ResponseEntity.ok("Frage gelöscht");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Frage nicht gefunden");
    }

    /**
     * Bulk-Import: Fügt nur neue Fragen ein, vorhandene werden übersprungen.
     */
    @PostMapping("/bulk")
    public ResponseEntity<String> bulkCreateQuestions(@RequestBody List<QuestionDto> questionDtos) {
        SaveResult result = questionService.saveBulk(questionDtos);
        return ResponseEntity.status(result.getStatus()).body(result.getMessage());
    }

    /**
     * Datei-Upload: Aktualisiert vorhandene Fragen und fügt neue ein.
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadQuestions(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Die Datei ist leer");
        }
        try {
            List<Question> question = objectMapper.readValue(file.getInputStream(), new TypeReference<List<Question>>() {});
            SaveResult result = questionService.saveAll(question);
            return ResponseEntity.status(result.getStatus()).body(result.getMessage());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Fehler beim Verarbeiten der Datei: " + e.getMessage());
        }
    }
}
