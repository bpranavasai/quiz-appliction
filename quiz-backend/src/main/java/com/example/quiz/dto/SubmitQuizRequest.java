package com.example.quiz.dto;

import java.util.Map;

public record SubmitQuizRequest(Map<Long, String> answers) {}
