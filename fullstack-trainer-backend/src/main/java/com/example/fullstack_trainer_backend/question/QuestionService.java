package com.example.fullstack_trainer_backend.question;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.example.fullstack_trainer_backend.question.category.Category;
import com.example.fullstack_trainer_backend.question.dtos.OptionDto;
import com.example.fullstack_trainer_backend.question.dtos.QuestionDto;
import com.example.fullstack_trainer_backend.question.option.Option;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<QuestionDto> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(this::convertToDto)  // Wandelt jede Frage in ein DTO um
                .collect(Collectors.toList());
    }
    private QuestionDto convertToDto(Question question) {
        QuestionDto dto = new QuestionDto();
        dto.setId(question.getId());  // Setze die ID
        dto.setText(question.getText());
        dto.setDifficulty(question.getDifficulty().name());
        dto.setExplanation(question.getExplanation());
        dto.setImageUrl(question.getImageUrl());
        dto.setMaxPoints(question.getMaxPoints());
    
        // Setze Kategorien
        dto.setCategories(question.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toList()));
        
        // Setze Optionen
        dto.setOptions(question.getOptions().stream()
                .map(option -> {
                    OptionDto optionDto = new OptionDto();
                    optionDto.setText(option.getText());
                    optionDto.setCorrect(option.isCorrect());
                    return optionDto;
                }).collect(Collectors.toList()));
        
        return dto;
    }
    

    // Liefert eine Frage anhand ihrer ID
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    /**
     * Speichert alle Fragen aus dem Datei-Upload.
     * Bestehende Fragen werden aktualisiert, neue hinzugefügt.
     *
     * @param questions Liste von Question Objekten (z. B. aus einer Datei)
     * @return SaveResult mit den erfolgreich gespeicherten Fragen und den Texten der Fragen, die fehlschlugen
     */
    public SaveResult saveAll(List<Question> questions) {
        List<Question> savedQuestions = new ArrayList<>();
        List<String> failedQuestionsText = new ArrayList<>();

        for (Question question : questions) {
            try {
                Optional<Question> existingOpt = questionRepository.findByText(question.getText());
                if (existingOpt.isPresent()) {
                    Question updated = updateExistingQuestion(existingOpt.get(), question);
                    savedQuestions.add(questionRepository.save(updated));
                } else {
                    savedQuestions.add(questionRepository.save(question));
                }
            } catch (Exception e) {
                failedQuestionsText.add(question.getText());
            }
        }
        return new SaveResult(savedQuestions, failedQuestionsText);
    }

    /**
     * Bulk-Import: Nur neue Fragen werden eingefügt.
     * Vorhandene Fragen bleiben unverändert.
     *
     * @param questionDtos Liste von QuestionDto Objekten
     * @return SaveResult mit den erfolgreich eingefügten Fragen und den übersprungenen (vorhandenen) Frage-Texten
     */
    public SaveResult saveBulk(List<QuestionDto> questionDtos) {
        List<Question> savedQuestions = new ArrayList<>();
        List<String> skippedQuestions = new ArrayList<>();

        for (QuestionDto dto : questionDtos) {
            try {
                // Existiert die Frage noch nicht?
                if (questionRepository.findByText(dto.getText()).isEmpty()) {
                    savedQuestions.add(questionRepository.save(convertToEntity(dto)));
                } else {
                    skippedQuestions.add(dto.getText());
                }
            } catch (Exception e) {
                skippedQuestions.add(dto.getText());
            }
        }
        return new SaveResult(savedQuestions, skippedQuestions);
    }

    /**
     * Erstellt eine neue Frage.
     * Wirft eine Exception, falls bereits eine Frage mit gleichem Text existiert.
     *
     * @param questionDTO Daten der neuen Frage
     * @return die gespeicherte Frage
     */
    public Question createQuestion(QuestionDto questionDTO) {
        if (questionRepository.findByText(questionDTO.getText()).isPresent()) {
            throw new IllegalArgumentException("Frage existiert bereits!");
        }
        return questionRepository.save(convertToEntity(questionDTO));
    }

    /**
     * Aktualisiert eine bestehende Frage anhand ihrer ID.
     *
     * @param id ID der zu aktualisierenden Frage
     * @param questionDTO Neue Daten
     * @return Optional der aktualisierten Frage oder leer, wenn nicht gefunden
     */
    public Optional<Question> updateQuestion(Long id, QuestionDto questionDTO) {
        return questionRepository.findById(id).map(existing -> {
            existing.setText(questionDTO.getText());
            existing.setDifficulty(DifficultyEnum.valueOf(questionDTO.getDifficulty()));
            existing.setExplanation(questionDTO.getExplanation());
            existing.setImageUrl(questionDTO.getImageUrl());
            existing.setMaxPoints(questionDTO.getMaxPoints());

            // Optionen aktualisieren
            existing.setOptions(
                questionDTO.getOptions().stream()
                    .map(this::convertOptionToEntity)
                    .peek(option -> option.setQuestion(existing))
                    .collect(Collectors.toList())
            );

            // Kategorien aktualisieren
            existing.setCategories(
                questionDTO.getCategories().stream()
                    .map(this::convertCategoryToEntity)
                    .collect(Collectors.toList())
            );

            return questionRepository.save(existing);
        });
    }

    /**
     * Löscht eine Frage anhand ihrer ID.
     *
     * @param id ID der zu löschenden Frage
     * @return true, wenn gelöscht, ansonsten false
     */
    public boolean deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            return false;
        }
        questionRepository.deleteById(id);
        return true;
    }

    /**
     * Aktualisiert alle Felder der bestehenden Frage mit den neuen Werten.
     *
     * @param existingQuestion Die vorhandene Frage
     * @param newQuestion      Neue Daten, die übernommen werden sollen
     * @return aktualisierte Frage
     */
    private Question updateExistingQuestion(Question existingQuestion, Question newQuestion) {
        existingQuestion.setText(newQuestion.getText());
        existingQuestion.setDifficulty(newQuestion.getDifficulty());
        existingQuestion.setExplanation(newQuestion.getExplanation());
        existingQuestion.setImageUrl(newQuestion.getImageUrl());
        existingQuestion.setMaxPoints(newQuestion.getMaxPoints());

        // Optionen aktualisieren
        existingQuestion.getOptions().clear();
        existingQuestion.getOptions().addAll(newQuestion.getOptions());
        existingQuestion.getOptions().forEach(option -> option.setQuestion(existingQuestion));

        // Kategorien aktualisieren
        existingQuestion.getCategories().clear();
        existingQuestion.getCategories().addAll(newQuestion.getCategories());

        return existingQuestion;
    }

    // Mapping von QuestionDto zu Question-Entity
    private Question convertToEntity(QuestionDto questionDTO) {
        Question question = new Question();
        question.setText(questionDTO.getText());
        question.setDifficulty(DifficultyEnum.valueOf(questionDTO.getDifficulty()));
        question.setExplanation(questionDTO.getExplanation());
        question.setImageUrl(questionDTO.getImageUrl());
        question.setMaxPoints(questionDTO.getMaxPoints());

        List<Option> options = questionDTO.getOptions().stream()
            .map(this::convertOptionToEntity)
            .peek(option -> option.setQuestion(question))
            .collect(Collectors.toList());
        question.setOptions(options);

        List<Category> categories = questionDTO.getCategories().stream()
            .map(this::convertCategoryToEntity)
            .collect(Collectors.toList());
        question.setCategories(categories);

        return question;
    }

    // Mapping von OptionDto zu Option-Entity
    private Option convertOptionToEntity(OptionDto optionDTO) {
        Option option = new Option();
        option.setText(optionDTO.getText());
        option.setCorrect(optionDTO.isCorrect());
        return option;
    }

    // Mapping von Kategorie (String) zu Category-Entity
    private Category convertCategoryToEntity(String categoryName) {
        Category category = new Category();
        category.setName(categoryName);
        return category;
    }
}
