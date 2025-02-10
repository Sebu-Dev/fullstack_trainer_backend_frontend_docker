package com.example.fullstack_trainer_backend.question;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fullstack_trainer_backend.question.answer.Answer;
import com.example.fullstack_trainer_backend.question.category.Category;
import com.example.fullstack_trainer_backend.question.dtos.AnswerDto;
import com.example.fullstack_trainer_backend.question.dtos.CategoryDto;
import com.example.fullstack_trainer_backend.question.dtos.QuestionDto;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id).orElseThrow(() -> new RuntimeException("Frage nicht gefunden"));
    }

    public Question createQuestion(QuestionDto questionDTO) {
        Question question = convertToEntity(questionDTO);
        return questionRepository.save(question);
    }

    public Question updateQuestion(Long id, QuestionDto questionDTO) {
        Question existingQuestion = getQuestionById(id);
        existingQuestion.setText(questionDTO.getText());
        existingQuestion.setTopic(questionDTO.getTopic());
        existingQuestion.setDifficulty(DifficultyEnum.valueOf(questionDTO.getDifficulty()));
        existingQuestion.setExplanation(questionDTO.getExplanation());
        existingQuestion.setImageUrl(questionDTO.getImageUrl());
        existingQuestion.setMaxPoints(questionDTO.getMaxPoints());

        existingQuestion.setOptions(questionDTO.getOptions().stream()
                .map(this::convertAnswerToEntity)
                .collect(Collectors.toList()));

        existingQuestion.setCategory(questionDTO.getCategory().stream()
                .map(this::convertCategoryToEntity)
                .collect(Collectors.toList()));

        return questionRepository.save(existingQuestion);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    public Question convertToEntity(QuestionDto questionDTO) {
        Question question = new Question();
        question.setText(questionDTO.getText());
        question.setTopic(questionDTO.getTopic());
        question.setDifficulty(DifficultyEnum.valueOf(questionDTO.getDifficulty()));
        question.setExplanation(questionDTO.getExplanation());
        question.setImageUrl(questionDTO.getImageUrl());
        question.setMaxPoints(questionDTO.getMaxPoints());

        question.setOptions(questionDTO.getOptions().stream()
                .map(this::convertAnswerToEntity)
                .collect(Collectors.toList()));

        question.setCategory(questionDTO.getCategory().stream()
                .map(this::convertCategoryToEntity)
                .collect(Collectors.toList()));

        return question;
    }

    public Answer convertAnswerToEntity(AnswerDto answerDTO) {
        Answer answer = new Answer();
        answer.setText(answerDTO.getText());
        answer.setIs_correct(answerDTO.isCorrect());
        return answer;
    }

    public Category convertCategoryToEntity(CategoryDto categoryDTO) {
        Category category = new Category();
        category.setCategory(categoryDTO.getName());
        return category;
    }
}
