package com.example.fullstack_trainer_backend.question;

import java.util.List;

import com.example.fullstack_trainer_backend.question.option.Option;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options;
    @Enumerated
    private DifficultyEnum difficulty;
    @OneToMany
    private List<String> category;
    private String explanation;
    private String imageUrl;
    private Integer maxPoints;
}
