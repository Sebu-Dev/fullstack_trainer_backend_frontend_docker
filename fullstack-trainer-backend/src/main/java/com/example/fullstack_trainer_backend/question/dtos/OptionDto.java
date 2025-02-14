package com.example.fullstack_trainer_backend.question.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class OptionDto {
    private String text;
    private boolean isCorrect;
    

}
