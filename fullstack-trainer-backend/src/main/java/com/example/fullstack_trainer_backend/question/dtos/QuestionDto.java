package com.example.fullstack_trainer_backend.question.dtos;

import java.util.List;

public class QuestionDto {
    private String text;
    private List<String> category;  
    private String difficulty;
    private List<OptionDto> options;  
    private String explanation;
    private String imageUrl;
    private Integer maxPoints;
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public List<String> getCategory() {
        return category;
    }
    public void setCategory(List<String> category) {
        this.category = category;
    }
    public String getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    public List<OptionDto> getOptions() {
        return options;
    }
    public void setOptions(List<OptionDto> options) {
        this.options = options;
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
