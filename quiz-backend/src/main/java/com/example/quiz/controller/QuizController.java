package com.example.quiz.controller;

import com.example.quiz.dto.QuestionView;
import com.example.quiz.dto.QuizResult;
import com.example.quiz.dto.QuizSummary;
import com.example.quiz.dto.SubmitQuizRequest;
import com.example.quiz.service.QuizService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    // Direct API endpoint to retrieve all quiz questions from MySQL
    @GetMapping("/questions")
    public List<QuestionView> getQuestions() {
        return quizService.getAllQuestions();
    }

    // Direct API endpoint to submit answers and get score
    @PostMapping("/submit")
    public QuizResult submit(@RequestBody(required = false) SubmitQuizRequest request) {
        return quizService.submitAnswers(request);
    }

    // Endpoints for frontend compatibility
    @GetMapping("/quizzes")
    public List<QuizSummary> listQuizzes() {
        return quizService.listQuizzes();
    }

    @GetMapping("/quizzes/{quizId}/questions")
    public List<QuestionView> getQuestionsForQuiz(@PathVariable Long quizId) {
        return quizService.getQuestionsForQuiz(quizId);
    }

    @PostMapping("/quizzes/{quizId}/submit")
    public QuizResult submitQuizAnswers(@PathVariable Long quizId, @RequestBody(required = false) SubmitQuizRequest request) {
        return quizService.submitAnswersForQuiz(quizId, request);
    }
}
