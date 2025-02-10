package com.example.fullstack_trainer_backend.question.dtos;

import java.util.List;

public class QuestionDto {
    private String text;
    private String topic;
    private List<AnswerDto> options;
    private String difficulty;
    private List<CategoryDto> category;
    private String explanation;
    private String imageUrl;
    private Integer maxPoints;

    // Getter und Setter
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<AnswerDto> getOptions() {
        return options;
    }

    public void setOptions(List<AnswerDto> options) {
        this.options = options;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<CategoryDto> getCategory() {
        return category;
    }

    public void setCategory(List<CategoryDto> category) {
        this.category = category;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(Integer maxPoints) {
        this.maxPoints = maxPoints;
    }
}
