package com.example.fullstack_trainer_backend.question;

import java.util.ArrayList;
import java.util.Optional;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.fullstack_trainer_backend.question.category.Category;
import com.example.fullstack_trainer_backend.question.dtos.OptionDto;
import com.example.fullstack_trainer_backend.question.dtos.QuestionDto;
import com.example.fullstack_trainer_backend.question.option.Option;
@Service
public class QuestionService {

    @Autowired
    
    private QuestionRepository questionRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Frage nicht gefunden"));
    }
    public SaveResult saveAll(List<Question> questions) {
        List<Question> savedQuestions = new ArrayList<>();
        List<String> failedQuestionsText = new ArrayList<>();
    
        for (Question question : questions) {
            try {
                Optional<Question> existingQuestion = questionRepository.findByText(question.getText());
    
                if (existingQuestion.isPresent()) {
                    // Wenn die Frage bereits existiert, führe ein Update durch
                    Question updatedQuestion = existingQuestion.get();
                    updatedQuestion.setText(question.getText()); 
                    savedQuestions.add(questionRepository.save(updatedQuestion));
                } 
                    savedQuestions.add(questionRepository.save(question));
                
            } catch (Exception e) {
                failedQuestionsText.add(question.getText());
            }
        }
        return new SaveResult(savedQuestions, failedQuestionsText);
    }
    



    public Question createQuestion(QuestionDto questionDTO) {
        Question question = convertToEntity(questionDTO);
        return questionRepository.save(question);
    }

    public Question updateQuestion(Long id, QuestionDto questionDTO) {
        Question existingQuestion = getQuestionById(id);
        existingQuestion.setText(questionDTO.getText());
        existingQuestion.setDifficulty(DifficultyEnum.valueOf(questionDTO.getDifficulty()));
        existingQuestion.setExplanation(questionDTO.getExplanation());
        existingQuestion.setImageUrl(questionDTO.getImageUrl());
        existingQuestion.setMaxPoints(questionDTO.getMaxPoints());
        // Optionen aktualisieren
        existingQuestion.setOptions(
            questionDTO.getOptions().stream()
                .map(this::convertOptionToEntity)
                .peek(option -> option.setQuestion(existingQuestion))
                .collect(Collectors.toList())
        );

        // Kategorien aktualisieren
        existingQuestion.setCategories(
            questionDTO.getCategories().stream()
                .map(this::convertCategoryToEntity)
                .collect(Collectors.toList())
        );

        return questionRepository.save(existingQuestion);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    // Mapping von QuestionDto zu Question-Entity
    public Question convertToEntity(QuestionDto questionDTO) {
        Question question = new Question();
        question.setText(questionDTO.getText());
        question.setDifficulty(DifficultyEnum.valueOf(questionDTO.getDifficulty()));
        question.setExplanation(questionDTO.getExplanation());
        question.setImageUrl(questionDTO.getImageUrl());
        question.setMaxPoints(questionDTO.getMaxPoints());

        // Mapping der Optionen
        List<Option> options = questionDTO.getOptions().stream()
                .map(this::convertOptionToEntity)
                .peek(option -> option.setQuestion(question))
                .collect(Collectors.toList());
        question.setOptions(options);

        // Mapping der Kategorien
        List<Category> categories = questionDTO.getCategories().stream()
                .map(this::convertCategoryToEntity)
                .collect(Collectors.toList());
        question.setCategories(categories);

        return question;
    }

    // Mapping von OptionDto zu Option-Entity
    public Option convertOptionToEntity(OptionDto optionDTO) {
        Option option = new Option();
        option.setText(optionDTO.getText());
        option.setCorrect(optionDTO.isCorrect());
        return option;
    }

    // Mapping von Kategorie (String) zu Category-Entity
    public Category convertCategoryToEntity(String categoryName) {
        Category category = new Category();
        category.setName(categoryName);
        return category;
    }
}
