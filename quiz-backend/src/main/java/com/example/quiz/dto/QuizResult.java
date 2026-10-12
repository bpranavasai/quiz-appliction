package com.example.quiz.dto;

public record QuizResult(int totalQuestions, int correctAnswers, int incorrectAnswers, double percentage, String message) {}
