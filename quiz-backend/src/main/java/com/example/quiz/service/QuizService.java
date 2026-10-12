package com.example.quiz.service;

import com.example.quiz.dto.QuestionView;
import com.example.quiz.dto.QuizResult;
import com.example.quiz.dto.QuizSummary;
import com.example.quiz.dto.SubmitQuizRequest;
import com.example.quiz.entity.Question;
import com.example.quiz.entity.Quiz;
import com.example.quiz.repository.QuestionRepository;
import com.example.quiz.repository.QuizRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    public QuizService(QuizRepository quizRepository, QuestionRepository questionRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }

    public List<QuizSummary> listQuizzes() {
        List<Quiz> quizzes = quizRepository.findAllByOrderByIdAsc();
        if (quizzes.isEmpty()) {
            int count = (int) questionRepository.count();
            return List.of(new QuizSummary(1L, "Quiz Application", "Answer the questions and check your score.", count));
        }

        return quizzes.stream()
                .map(q -> {
                    int count = (int) questionRepository.countByQuizId(q.getId());
                    return new QuizSummary(q.getId(), q.getTitle(), q.getDescription(), count);
                })
                .toList();
    }

    public List<QuestionView> getAllQuestions() {
        List<Question> questions = questionRepository.findAllByOrderByIdAsc();
        return questions.stream().map(this::toView).toList();
    }

    public List<QuestionView> getQuestionsForQuiz(Long quizId) {
        List<Question> questions = (quizId != null)
                ? questionRepository.findByQuizIdOrderByIdAsc(quizId)
                : List.of();

        if (questions.isEmpty()) {
            questions = questionRepository.findAllByOrderByIdAsc();
        }

        return questions.stream().map(this::toView).toList();
    }

    public QuizResult submitAnswersForQuiz(Long quizId, SubmitQuizRequest request) {
        List<Question> questions = (quizId != null)
                ? questionRepository.findByQuizIdOrderByIdAsc(quizId)
                : List.of();

        if (questions.isEmpty()) {
            questions = questionRepository.findAllByOrderByIdAsc();
        }

        return evaluateAnswers(questions, request);
    }

    public QuizResult submitAnswers(SubmitQuizRequest request) {
        List<Question> questions = questionRepository.findAllByOrderByIdAsc();
        return evaluateAnswers(questions, request);
    }

    private QuizResult evaluateAnswers(List<Question> questions, SubmitQuizRequest request) {
        if (questions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No questions found in database");
        }

        Map<Long, String> answers = (request != null && request.answers() != null)
                ? request.answers()
                : Map.of();

        int correct = 0;
        for (Question q : questions) {
            String chosen = answers.get(q.getId());
            if (chosen != null && q.getCorrectAnswer() != null
                    && chosen.trim().equalsIgnoreCase(q.getCorrectAnswer().trim())) {
                correct++;
            }
        }

        int total = questions.size();
        int incorrect = total - correct;
        double percentage = Math.round((correct * 10000.0) / total) / 100.0;
        String message = percentage >= 80.0
                ? "Outstanding! Keep up the brilliant work."
                : percentage >= 50.0
                ? "Great work! Keep learning."
                : "Keep practising—you can improve!";

        return new QuizResult(total, correct, incorrect, percentage, message);
    }

    private QuestionView toView(Question q) {
        return new QuestionView(
                q.getId(),
                q.getQuestion(),
                q.getOptionA(),
                q.getOptionB(),
                q.getOptionC(),
                q.getOptionD()
        );
    }
}
