package com.example.quiz.dto;

public record QuestionView(
    Long id,
    String question,
    String text,
    String optionA,
    String optionB,
    String optionC,
    String optionD
) {
    public QuestionView(Long id, String question, String optionA, String optionB, String optionC, String optionD) {
        this(id, question, question, optionA, optionB, optionC, optionD);
    }
}
