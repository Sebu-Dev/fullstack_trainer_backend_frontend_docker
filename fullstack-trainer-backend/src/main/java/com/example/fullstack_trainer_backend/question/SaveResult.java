package com.example.fullstack_trainer_backend.question;
import java.util.List;

public class SaveResult {
    private List<Question> savedQuestions;
    private List<String> failedQuestionsText;

    public SaveResult(List<Question> savedQuestions, List<String> failedQuestionsText) {
        this.savedQuestions = savedQuestions;
        this.failedQuestionsText = failedQuestionsText;
    }

    // Getter und Setter
    public List<Question> getSavedQuestions() {
        return savedQuestions;
    }

    public void setSavedQuestions(List<Question> savedQuestions) {
        this.savedQuestions = savedQuestions;
    }

    public List<String> getFailedQuestionsText() {
        return failedQuestionsText;
    }

    public void setFailedQuestionsText(List<String> failedQuestionsText) {
        this.failedQuestionsText = failedQuestionsText;
    }
}
