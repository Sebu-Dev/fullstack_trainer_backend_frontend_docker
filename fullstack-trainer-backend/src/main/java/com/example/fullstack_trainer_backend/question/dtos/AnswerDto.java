package com.example.fullstack_trainer_backend.question.dtos;

public class AnswerDto {
    private String text;
    private boolean isCorrect;

    // Getter und Setter
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}
