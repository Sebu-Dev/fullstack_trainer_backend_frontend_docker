package com.example.fullstack_trainer_backend.question;

import java.util.List;

import com.example.fullstack_trainer_backend.question.category.Category;
import com.example.fullstack_trainer_backend.question.option.Option;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@Entity
@Table(name = "questions")

public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT",unique = true)
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR")
    private DifficultyEnum difficulty;
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "question_category",
        joinColumns = @JoinColumn(name = "question_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;
    
    @Column(columnDefinition = "TEXT")
    private String explanation;
    @Column(columnDefinition = "TEXT")
    private String imageUrl;
    private Integer maxPoints;
}