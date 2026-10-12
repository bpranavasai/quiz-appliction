import { useEffect, useState } from 'react';

const API = (import.meta.env.VITE_API_URL || 'http://localhost:8080/api').replace(/\/$/, '');
const LETTERS = ['A', 'B', 'C', 'D'];

const ICONS = {
  'Java Programming': '☕',
  'Python Programming': '🐍',
  'General Knowledge': '🌍',
  'Computer Science': '💻',
  'Electronics and Digital Logic': '⚡'
};

export default function App() {
  const [quizzes, setQuizzes] = useState([]);
  const [quiz, setQuiz] = useState(null);
  const [questions, setQuestions] = useState([]);
  const [answers, setAnswers] = useState({});
  const [index, setIndex] = useState(0);
  const [seconds, setSeconds] = useState(120);
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    fetch(`${API}/quizzes`)
      .then(r => { if (!r.ok) throw new Error('Could not load quizzes.'); return r.json(); })
      .then(data => setQuizzes(data))
      .catch(() => setError('Could not connect to the backend. Start Spring Boot and check the API URL.'))
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => {
    if (!quiz || result || submitting) return;
    if (seconds <= 0) { submitQuiz(); return; }
    const timer = setTimeout(() => setSeconds(s => s - 1), 1000);
    return () => clearTimeout(timer);
  }, [quiz, result, submitting, seconds]);

  async function startQuiz(item) {
    setError(''); setLoading(true);
    try {
      const response = await fetch(`${API}/quizzes/${item.id}/questions`);
      if (!response.ok) throw new Error('Could not load questions.');
      const data = await response.json();
      if (!data.length) throw new Error('This quiz has no questions yet.');
      setQuiz(item); setQuestions(data); setAnswers({}); setIndex(0); setSeconds(120); setResult(null);
    } catch (e) { setError(e.message || 'Unable to start quiz.'); }
    finally { setLoading(false); }
  }

  async function submitQuiz() {
    if (!quiz || submitting || result) return;
    setSubmitting(true); setError('');
    try {
      const response = await fetch(`${API}/quizzes/${quiz.id}/submit`, {
        method: 'POST', headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ answers })
      });
      if (!response.ok) throw new Error('Could not submit your answers. Please try again.');
      setResult(await response.json());
    } catch (e) { setError(e.message || 'Submission failed.'); }
    finally { setSubmitting(false); }
  }

  function restart() {
    setQuiz(null); setQuestions([]); setAnswers({}); setIndex(0); setSeconds(120); setResult(null); setError('');
  }

  function formatTime(value) {
    return `${String(Math.floor(value / 60)).padStart(2, '0')}:${String(value % 60).padStart(2, '0')}`;
  }

  const current = questions[index];
  const optionValues = current ? [current.optionA, current.optionB, current.optionC, current.optionD] : [];
  const totalQuestionsAvailable = quizzes.reduce((acc, q) => acc + (q.questionCount || 0), 0);

  return (
    <main className="page-shell">
      <header className="topbar">
        <a className="brand" href="#home" onClick={e => { e.preventDefault(); restart(); }}>
          <span className="brand-mark">Q</span> QuizUp
        </a>
        <span className="top-note">LEARN SOMETHING NEW</span>
      </header>

      {error && <div className="error-banner" role="alert">{error}</div>}

      {loading && !quiz && (
        <section className="center-card">
          <div className="loader" />
          <p>Loading your quizzes...</p>
        </section>
      )}

      {!loading && !quiz && !result && (
        <>
          <section className="hero">
            <div className="hero-copy">
              <span className="eyebrow"><span className="dot" /> CHOOSE YOUR TOPIC</span>
              <h1>Test your skills.<br /><span>Choose your quiz.</span></h1>
              <p>Explore curated quizzes across programming languages, general knowledge, computer science, and digital logic.</p>
              <div className="hero-stats">
                <div><strong>{quizzes.length}</strong><span>Topics</span></div>
                <div><strong>{totalQuestionsAvailable || 25}</strong><span>Questions</span></div>
                <div><strong>2 min</strong><span>Per Quiz</span></div>
              </div>
            </div>

            {quizzes.length > 0 && (
              <div className="quiz-card intro-card">
                <div className="card-illustration">
                  <span className="spark spark-one">✦</span>
                  <span className="book-icon">{ICONS[quizzes[0].title] || '▤'}</span>
                  <span className="spark spark-two">✧</span>
                </div>
                <span className="eyebrow">FEATURED QUIZ</span>
                <h2>{quizzes[0].title}</h2>
                <p>{quizzes[0].description}</p>
                <div className="card-meta">
                  <span>◷ 2 minutes</span>
                  <span>• {quizzes[0].questionCount || 5} Questions</span>
                  <span>• Multiple choice</span>
                </div>
                <button className="primary-btn full-btn" onClick={() => startQuiz(quizzes[0])}>
                  Start featured quiz <span>→</span>
                </button>
              </div>
            )}
          </section>

          <section className="quiz-catalog">
            <div className="quiz-catalog-header">
              <span className="eyebrow"><span className="dot" /> AVAILABLE QUIZZES</span>
              <h2>All Quiz Topics</h2>
              <p>Select any category below to begin your timed 2-minute challenge.</p>
            </div>

            <div className="quiz-grid">
              {quizzes.map((item) => (
                <div key={item.id} className="quiz-select-card">
                  <div className="quiz-card-top">
                    <div className="quiz-icon-box">{ICONS[item.title] || '✦'}</div>
                    <span className="quiz-count-pill">{item.questionCount || 5} Questions</span>
                  </div>
                  <h3>{item.title}</h3>
                  <p>{item.description}</p>
                  <div className="card-meta">
                    <span>◷ 2 minutes</span>
                    <span>• 4 options</span>
                  </div>
                  <button className="primary-btn full-btn" onClick={() => startQuiz(item)}>
                    Start this quiz <span>→</span>
                  </button>
                </div>
              ))}
            </div>
          </section>
        </>
      )}

      {quiz && !result && current && (
        <section className="quiz-layout">
          <div className="quiz-heading">
            <span className="eyebrow">{quiz.title.toUpperCase()}</span>
            <h1>You've got this.</h1>
            <p>Take your time, choose the best answer.</p>
          </div>

          <div className="quiz-card question-card">
            <div className="question-top">
              <span className="question-count">
                QUESTION <b>{index + 1}</b> <span>/ {questions.length}</span>
              </span>
              <span className={`timer ${seconds <= 20 ? 'timer-warning' : ''}`}>
                ◷ {formatTime(seconds)}
              </span>
            </div>

            <div className="progress-track">
              <div
                className="progress-fill"
                style={{ width: `${((index + 1) / questions.length) * 100}%` }}
              />
            </div>

            <h2 className="question-text">{current.text || current.question}</h2>

            <div className="options-list">
              {optionValues.map((option, i) => (
                <button
                  key={LETTERS[i]}
                  className={`option ${answers[current.id] === LETTERS[i] ? 'selected' : ''}`}
                  onClick={() => setAnswers(old => ({ ...old, [current.id]: LETTERS[i] }))}
                >
                  <span className="option-letter">{LETTERS[i]}</span>
                  <span>{option}</span>
                  <span className="option-check">{answers[current.id] === LETTERS[i] ? '✓' : ''}</span>
                </button>
              ))}
            </div>

            <div className="question-footer">
              <span className="answered-count">
                {Object.keys(answers).length} of {questions.length} answered
              </span>
              <div className="nav-buttons">
                {index > 0 && (
                  <button className="secondary-btn" onClick={() => setIndex(i => i - 1)}>
                    ← Back
                  </button>
                )}
                {index < questions.length - 1 ? (
                  <button className="primary-btn" onClick={() => setIndex(i => i + 1)}>
                    Next question →
                  </button>
                ) : (
                  <button
                    className="primary-btn"
                    disabled={submitting}
                    onClick={submitQuiz}
                  >
                    {submitting ? 'Submitting...' : 'Finish quiz ✓'}
                  </button>
                )}
              </div>
            </div>
          </div>
          <p className="quiz-tip">TIP: There are no penalties for unanswered questions.</p>
        </section>
      )}

      {result && (
        <section className="result-wrap">
          <div className="quiz-card result-card">
            <div className="result-icon">{result.percentage >= 50 ? '✦' : '↗'}</div>
            <span className="eyebrow">{quiz?.title ? quiz.title.toUpperCase() : 'QUIZ COMPLETE'}</span>
            <h1>
              {result.percentage >= 80
                ? 'Brilliant work!'
                : result.percentage >= 50
                ? 'Nice work!'
                : 'Keep going!'}
            </h1>
            <p className="result-message">{result.message}</p>
            <div className="score-circle">
              <strong>{result.percentage}%</strong>
              <span>YOUR SCORE</span>
            </div>
            <div className="result-stats">
              <div>
                <strong>{result.correctAnswers}</strong>
                <span>Correct</span>
              </div>
              <div>
                <strong>{result.incorrectAnswers}</strong>
                <span>Incorrect / skipped</span>
              </div>
              <div>
                <strong>{result.totalQuestions}</strong>
                <span>Total questions</span>
              </div>
            </div>
            <button className="primary-btn full-btn" onClick={restart}>
              Choose another quiz <span>→</span>
            </button>
          </div>
        </section>
      )}

      <footer className="footer">
        <span>© 2026 QuizUp</span>
        <span>Curiosity looks good on you <span className="heart">♥</span></span>
      </footer>
    </main>
  );
}
