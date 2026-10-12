package com.example.quiz.config;

import com.example.quiz.entity.Question;
import com.example.quiz.entity.Quiz;
import com.example.quiz.repository.QuestionRepository;
import com.example.quiz.repository.QuizRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initData(QuizRepository quizRepository, QuestionRepository questionRepository) {
        return args -> {
            if (quizRepository.count() == 0) {
                Quiz q1 = quizRepository.save(new Quiz("Java Programming", "Test your knowledge of core Java concepts, OOP, memory management, and JVM fundamentals."));
                Quiz q2 = quizRepository.save(new Quiz("Python Programming", "Challenge your understanding of Python syntax, data structures, and standard library behaviors."));
                Quiz q3 = quizRepository.save(new Quiz("General Knowledge", "Broaden your horizons with trivia about geography, science, history, and world facts."));
                Quiz q4 = quizRepository.save(new Quiz("Computer Science", "Test your grasp on algorithms, data structures, operating systems, and networking."));
                Quiz q5 = quizRepository.save(new Quiz("Electronics and Digital Logic", "Explore semiconductor devices, boolean algebra, logic gates, and digital circuits."));

                if (questionRepository.count() == 0) {
                    questionRepository.saveAll(List.of(
                        new Question(q1.getId(), "Which of the following is not a Java primitive data type?", "int", "boolean", "String", "char", "C"),
                        new Question(q1.getId(), "What is the default value of an uninitialized boolean instance variable in Java?", "true", "false", "null", "0", "B"),
                        new Question(q1.getId(), "Which keyword is used to prevent method overriding in Java?", "static", "const", "final", "abstract", "C"),
                        new Question(q1.getId(), "Which collection class in java.util allows null keys and null values?", "Hashtable", "TreeMap", "HashMap", "ConcurrentHashMap", "C"),
                        new Question(q1.getId(), "In Java, which memory area stores class structures, method data, and bytecode?", "Stack", "Metaspace", "PC Register", "Native Method Stack", "B"),

                        new Question(q2.getId(), "Which of the following data types is immutable in Python?", "list", "dict", "tuple", "set", "C"),
                        new Question(q2.getId(), "What does bool([]) evaluate to in Python?", "True", "False", "None", "TypeError", "B"),
                        new Question(q2.getId(), "Which keyword is used to define an anonymous (lambda) function in Python?", "def", "func", "lambda", "anonymous", "C"),
                        new Question(q2.getId(), "How do you append an element to the end of a list in Python?", "list.push(x)", "list.append(x)", "list.add(x)", "list.insert(x)", "B"),
                        new Question(q2.getId(), "Which statement is used for exception handling in Python?", "try...catch", "try...except", "do...catch", "attempt...rescue", "B"),

                        new Question(q3.getId(), "What is the capital of India?", "Mumbai", "Delhi", "Hyderabad", "Chennai", "B"),
                        new Question(q3.getId(), "Which planet is popularly known as the Red Planet?", "Venus", "Mars", "Jupiter", "Saturn", "B"),
                        new Question(q3.getId(), "What is the largest ocean on Earth?", "Atlantic Ocean", "Indian Ocean", "Arctic Ocean", "Pacific Ocean", "D"),
                        new Question(q3.getId(), "Who wrote the play Romeo and Juliet?", "William Shakespeare", "Charles Dickens", "Mark Twain", "Jane Austen", "A"),
                        new Question(q3.getId(), "Which chemical element has the symbol O?", "Gold", "Osmium", "Oxygen", "Iron", "C"),

                        new Question(q4.getId(), "What is the worst-case time complexity of QuickSort?", "O(n log n)", "O(n)", "O(n^2)", "O(log n)", "C"),
                        new Question(q4.getId(), "Which data structure follows the First-In-First-Out (FIFO) principle?", "Stack", "Queue", "Binary Tree", "Heap", "B"),
                        new Question(q4.getId(), "In networking, which protocol resolves domain names to IP addresses?", "DHCP", "HTTP", "DNS", "FTP", "C"),
                        new Question(q4.getId(), "Which CPU scheduling algorithm may lead to process starvation?", "Round Robin", "Priority Scheduling", "First-Come First-Served", "Shortest Remaining Time with Aging", "B"),
                        new Question(q4.getId(), "What is the primary function of Virtual Memory in modern operating systems?", "Increase CPU clock speed", "Allow processes larger than physical RAM to execute", "Speed up network transmission", "Encrypt secondary storage", "B"),

                        new Question(q5.getId(), "Which logic gate gives a LOW (0) output only when all inputs are HIGH (1)?", "AND", "NAND", "OR", "XOR", "B"),
                        new Question(q5.getId(), "What is the base of the hexadecimal number system?", "2", "8", "10", "16", "D"),
                        new Question(q5.getId(), "Which semiconductor component converts AC voltage to pulsating DC voltage?", "Transformer", "Rectifier diode", "Inverter", "Capacitor", "B"),
                        new Question(q5.getId(), "How many flip-flops are required to construct a MOD-16 ripple counter?", "2", "4", "8", "16", "B"),
                        new Question(q5.getId(), "According to De Morgans theorem, the complement of (A AND B) equals:", "(NOT A) OR (NOT B)", "(NOT A) AND (NOT B)", "A OR B", "NOT (A OR B)", "A")
                    ));
                }
            }
        };
    }
}
